package com.example.payment_worker.saga;

import com.example.payment_worker.entity.Payment;
import com.example.payment_worker.event.*;
import com.example.payment_worker.repository.PaymentRepository;
import com.example.payment_worker.service.OutboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class PaymentSagaOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSagaOrchestrator.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OutboxService outboxService;

    /**
     * Validate payment (simulated validation logic)
     */
    private boolean isValidPayment(Payment payment) {
        // Basic validation: check if amount is positive and currency is supported
        return payment.getAmount() != null &&
               payment.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0 &&
               payment.getCurrency() != null &&
               !payment.getCurrency().isEmpty();
    }

    /**
     * Handle PaymentInitiatedEvent - Start the payment processing saga
     */
    @Transactional
    public void handlePaymentInitiated(PaymentInitiatedEvent event) {
        LOGGER.info("Starting payment saga for transaction: {}", event.getTransactionId());

        try {
            // Find the payment
            Payment payment = paymentRepository.findById(event.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + event.getPaymentId()));

            // Initialize saga
            payment.initiateSaga(event.getTransactionId());
            payment.startProcessing();

            // Perform basic validation (simulated)
            if (isValidPayment(payment)) {
                // Save payment and publish PaymentProcessedEvent
                paymentRepository.save(payment);

                PaymentProcessedEvent processedEvent = new PaymentProcessedEvent(
                    event.getTransactionId(),
                    event.getOrderId(),
                    event.getAmount(),
                    event.getCurrency(),
                    event.getPaymentMethod(),
                    event.getPaymentId()
                );

                outboxService.saveEvent("Payment", event.getPaymentId().toString(),
                    "PaymentProcessedEvent", processedEvent);

                LOGGER.info("Payment processing started for transaction: {}", event.getTransactionId());
            } else {
                // Payment validation failed
                payment.failSaga("Payment validation failed");
                paymentRepository.save(payment);

                PaymentFailedEvent failedEvent = new PaymentFailedEvent(
                    event.getTransactionId(),
                    event.getOrderId(),
                    event.getPaymentId(),
                    "Payment validation failed"
                );

                outboxService.saveEvent("Payment", event.getPaymentId().toString(),
                    "PaymentFailedEvent", failedEvent);

                LOGGER.warn("Payment validation failed for transaction: {}", event.getTransactionId());
            }

        } catch (Exception e) {
            LOGGER.error("Error handling PaymentInitiatedEvent for transaction: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to handle payment initiation", e);
        }
    }

    /**
     * Handle AccountUpdatedEvent - Complete the payment saga
     */
    @Transactional
    public void handleAccountUpdated(AccountUpdatedEvent event) {
        LOGGER.info("Completing payment saga for transaction: {}", event.getTransactionId());

        try {
            // Find the payment
            Payment payment = paymentRepository.findById(event.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + event.getPaymentId()));

            // Update payment status
            payment.accountUpdated();
            payment.completeSaga();

            // Save payment and publish PaymentCompletedEvent
            paymentRepository.save(payment);

            PaymentCompletedEvent completedEvent = new PaymentCompletedEvent(
                event.getTransactionId(),
                event.getOrderId(),
                event.getPaymentId(),
                event.getAmount(),
                event.getCurrency(),
                payment.getPaymentMethod()
            );

            outboxService.saveEvent("Payment", event.getPaymentId().toString(),
                "PaymentCompletedEvent", completedEvent);

            LOGGER.info("Payment saga completed for transaction: {}", event.getTransactionId());

        } catch (Exception e) {
            LOGGER.error("Error completing payment saga for transaction: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to complete payment saga", e);
        }
    }

    /**
     * Handle compensating transaction for failed account update
     */
    @Transactional
    public void handleAccountUpdateFailed(AccountUpdatedEvent event, String failureReason) {
        LOGGER.warn("Handling account update failure for transaction: {}", event.getTransactionId());

        try {
            // Find the payment
            Payment payment = paymentRepository.findById(event.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + event.getPaymentId()));

            // Cancel the payment saga
            payment.cancelSaga(failureReason);
            paymentRepository.save(payment);

            PaymentCanceledEvent canceledEvent = new PaymentCanceledEvent(
                event.getTransactionId(),
                event.getOrderId(),
                event.getPaymentId(),
                failureReason
            );

            outboxService.saveEvent("Payment", event.getPaymentId().toString(),
                "PaymentCanceledEvent", canceledEvent);

            LOGGER.info("Payment saga canceled for transaction: {}", event.getTransactionId());

        } catch (Exception e) {
            LOGGER.error("Error canceling payment saga for transaction: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to cancel payment saga", e);
        }
    }
}
