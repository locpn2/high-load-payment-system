package com.example.payment_worker.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.payment_worker.dto.PaymentRequest;
import com.example.payment_worker.entity.Payment;
import com.example.payment_worker.repository.PaymentRepository;
import com.google.gson.Gson;

import java.math.BigDecimal;

@Component
public class PaymentWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentWorker.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Gson gson;

    public PaymentWorker() {
        this.gson = new Gson();
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void processPayment(String message) {
        LOGGER.info("Received payment message from Kafka: {}", message);

        try {
            // Parse JSON message to PaymentRequest
            PaymentRequest paymentRequest = gson.fromJson(message, PaymentRequest.class);
            LOGGER.info("Processing payment for order: {}", paymentRequest.getOrderId());

            // Convert PaymentRequest to Payment entity
            Payment payment = convertToEntity(paymentRequest);
            payment.setStatus("PROCESSED");

            // Save to database
            Payment savedPayment = paymentRepository.save(payment);
            LOGGER.info("Payment processed successfully with ID: {}", savedPayment.getId());

            // Send cache invalidation message to Kafka
            try {
                String orderId = paymentRequest.getOrderId();
                if (orderId != null) {
                    kafkaTemplate.send("payment-updates", orderId);
                    LOGGER.info("Cache invalidation message sent for order: {}", orderId);
                } else {
                    LOGGER.warn("OrderId is null, cannot send cache invalidation message");
                }
            } catch (Exception e) {
                LOGGER.error("Error sending cache invalidation message: {}", e.getMessage(), e);
                // Note: We don't fail the payment processing if cache invalidation fails
            }

        } catch (Exception e) {
            LOGGER.error("Error processing payment message: {}", e.getMessage(), e);
            // In a production system, you might want to:
            // 1. Send message to dead letter queue
            // 2. Implement retry mechanism
            // 3. Send notification to monitoring system
        }
    }

    private Payment convertToEntity(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        return payment;
    }
}
