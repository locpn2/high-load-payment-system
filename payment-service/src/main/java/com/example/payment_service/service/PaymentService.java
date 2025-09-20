package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    private final Gson gson;

    public PaymentService() {
        this.gson = new Gson();
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment createPayment(Payment payment) {
        // Send payment request to Kafka for async processing
        try {
            PaymentRequest paymentRequest = convertToPaymentRequest(payment);
            String message = gson.toJson(paymentRequest);
            kafkaTemplate.send(kafkaTopic, message);
            LOGGER.info("Payment message sent to Kafka topic: {}", kafkaTopic);

            // Set status to indicate it's being processed
            payment.setStatus("PROCESSING");
            return payment;
        } catch (Exception e) {
            LOGGER.error("Failed to send payment message to Kafka: {}", e.getMessage());
            // In production, you might want to implement a retry mechanism or dead letter queue
            payment.setStatus("FAILED");
            return payment;
        }
    }

    public Payment updatePayment(Long id, Payment payment) {
        Payment existingPayment = paymentRepository.findById(id).orElse(null);
        if (existingPayment != null) {
            payment.setId(id);
            return paymentRepository.save(payment);
        }
        return null;
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    private PaymentRequest convertToPaymentRequest(Payment payment) {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(payment.getOrderId());
        request.setAmount(payment.getAmount());
        request.setCurrency(payment.getCurrency());
        request.setPaymentMethod(payment.getPaymentMethod());
        return request;
    }
}
