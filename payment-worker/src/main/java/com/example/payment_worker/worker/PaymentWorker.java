package com.example.payment_worker.worker;

import com.example.payment_worker.event.*;
import com.example.payment_worker.saga.PaymentSagaOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class PaymentWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentWorker.class);

    @Autowired
    private PaymentSagaOrchestrator sagaOrchestrator;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Gson gson;

    public PaymentWorker() {
        this.gson = new Gson();
    }

    /**
     * Listen for PaymentInitiatedEvent from payment-service
     */
    @KafkaListener(topics = "payment-initiated", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentInitiated(String message) {
        LOGGER.info("Received PaymentInitiatedEvent: {}", message);

        try {
            PaymentInitiatedEvent event = gson.fromJson(message, PaymentInitiatedEvent.class);
            sagaOrchestrator.handlePaymentInitiated(event);
        } catch (Exception e) {
            LOGGER.error("Error handling PaymentInitiatedEvent: {}", e.getMessage(), e);
        }
    }

    /**
     * Listen for AccountUpdatedEvent from account-service
     */
    @KafkaListener(topics = "account-updated", groupId = "${spring.kafka.consumer.group-id}")
    public void handleAccountUpdated(String message) {
        LOGGER.info("Received AccountUpdatedEvent: {}", message);

        try {
            AccountUpdatedEvent event = gson.fromJson(message, AccountUpdatedEvent.class);
            sagaOrchestrator.handleAccountUpdated(event);
        } catch (Exception e) {
            LOGGER.error("Error handling AccountUpdatedEvent: {}", e.getMessage(), e);
            // Handle compensating transaction
            try {
                AccountUpdatedEvent failedEvent = gson.fromJson(message, AccountUpdatedEvent.class);
                sagaOrchestrator.handleAccountUpdateFailed(failedEvent, e.getMessage());
            } catch (Exception ex) {
                LOGGER.error("Error handling compensating transaction: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * Listen for AccountUpdateFailedEvent from account-service
     */
    @KafkaListener(topics = "account-update-failed", groupId = "${spring.kafka.consumer.group-id}")
    public void handleAccountUpdateFailed(String message) {
        LOGGER.warn("Received AccountUpdateFailedEvent: {}", message);

        try {
            AccountUpdatedEvent event = gson.fromJson(message, AccountUpdatedEvent.class);
            String failureReason = "Account update failed"; // Could be extracted from message if needed
            sagaOrchestrator.handleAccountUpdateFailed(event, failureReason);
        } catch (Exception e) {
            LOGGER.error("Error handling AccountUpdateFailedEvent: {}", e.getMessage(), e);
        }
    }

    /**
     * Legacy listener for backward compatibility (can be removed after migration)
     */
    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}-legacy")
    public void processLegacyPayment(String message) {
        LOGGER.info("Received legacy payment message from Kafka: {}", message);
        LOGGER.warn("Legacy payment processing is deprecated. Use Saga pattern instead.");
        // This can be removed once all clients migrate to the new event-driven approach
    }
}
