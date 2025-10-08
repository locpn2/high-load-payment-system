package com.example.payment_worker.saga;

import com.example.payment_worker.entity.Payment;
import com.example.payment_worker.event.*;
import com.example.payment_worker.repository.PaymentRepository;
import com.example.payment_worker.service.OutboxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentSagaOrchestratorTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private PaymentSagaOrchestrator orchestrator;

    private UUID transactionId;
    private Long paymentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionId = UUID.randomUUID();
        paymentId = 1L;
    }

    @Test
    void handlePaymentInitiated_ValidPayment_ShouldProcessSuccessfully() {
        // Arrange
        PaymentInitiatedEvent event = new PaymentInitiatedEvent();
        event.setTransactionId(transactionId);
        event.setOrderId("order123");
        event.setAmount(BigDecimal.valueOf(100.00));
        event.setCurrency("USD");
        event.setPaymentMethod("credit_card");
        event.setPaymentId(paymentId);

        Payment payment = createValidPayment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // Act
        orchestrator.handlePaymentInitiated(event);

        // Assert
        verify(paymentRepository).save(payment);
        verify(outboxService).saveEvent(eq("Payment"), eq(paymentId.toString()),
            eq("PaymentProcessedEvent"), any(PaymentProcessedEvent.class));
        assert payment.getSagaStatus() == Payment.SagaStatus.PROCESSING;
    }

    @Test
    void handlePaymentInitiated_InvalidPayment_ShouldFail() {
        // Arrange
        PaymentInitiatedEvent event = new PaymentInitiatedEvent();
        event.setTransactionId(transactionId);
        event.setOrderId("order123");
        event.setAmount(BigDecimal.valueOf(-100.00)); // Invalid amount
        event.setCurrency("USD");
        event.setPaymentMethod("credit_card");
        event.setPaymentId(paymentId);

        Payment payment = createInvalidPayment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // Act
        orchestrator.handlePaymentInitiated(event);

        // Assert
        verify(paymentRepository).save(payment);
        verify(outboxService).saveEvent(eq("Payment"), eq(paymentId.toString()),
            eq("PaymentFailedEvent"), any(PaymentFailedEvent.class));
        assert payment.getSagaStatus() == Payment.SagaStatus.FAILED;
    }

    @Test
    void handleAccountUpdated_ShouldCompleteSaga() {
        // Arrange
        AccountUpdatedEvent event = new AccountUpdatedEvent();
        event.setTransactionId(transactionId);
        event.setOrderId("order123");
        event.setPaymentId(paymentId);
        event.setAmount(BigDecimal.valueOf(100.00));
        event.setCurrency("USD");
        event.setAccountId("account123");

        Payment payment = createProcessingPayment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // Act
        orchestrator.handleAccountUpdated(event);

        // Assert
        verify(paymentRepository).save(payment);
        verify(outboxService).saveEvent(eq("Payment"), eq(paymentId.toString()),
            eq("PaymentCompletedEvent"), any(PaymentCompletedEvent.class));
        assert payment.getSagaStatus() == Payment.SagaStatus.COMPLETED;
    }

    @Test
    void handleAccountUpdateFailed_ShouldCancelSaga() {
        // Arrange
        AccountUpdatedEvent event = new AccountUpdatedEvent();
        event.setTransactionId(transactionId);
        event.setOrderId("order123");
        event.setPaymentId(paymentId);
        event.setAmount(BigDecimal.valueOf(100.00));
        event.setCurrency("USD");
        event.setAccountId("account123");

        Payment payment = createProcessingPayment();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // Act
        orchestrator.handleAccountUpdateFailed(event, "Account update failed");

        // Assert
        verify(paymentRepository).save(payment);
        verify(outboxService).saveEvent(eq("Payment"), eq(paymentId.toString()),
            eq("PaymentCanceledEvent"), any(PaymentCanceledEvent.class));
        assert payment.getSagaStatus() == Payment.SagaStatus.CANCELED;
    }

    private Payment createValidPayment() {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setOrderId("order123");
        payment.setAmount(BigDecimal.valueOf(100.00));
        payment.setCurrency("USD");
        payment.setPaymentMethod("credit_card");
        payment.setStatus("PENDING");
        return payment;
    }

    private Payment createInvalidPayment() {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setOrderId("order123");
        payment.setAmount(BigDecimal.valueOf(-100.00)); // Invalid
        payment.setCurrency("USD");
        payment.setPaymentMethod("credit_card");
        payment.setStatus("PENDING");
        return payment;
    }

    private Payment createProcessingPayment() {
        Payment payment = createValidPayment();
        payment.setSagaStatus(Payment.SagaStatus.PROCESSING);
        return payment;
    }
}
