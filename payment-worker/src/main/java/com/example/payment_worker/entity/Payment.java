package com.example.payment_worker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String status;

    // Saga Pattern fields
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SagaStatus sagaStatus = SagaStatus.PENDING;

    @Column(nullable = false)
    private UUID transactionId;

    @Column
    private String failureReason;

    public enum SagaStatus {
        PENDING,        // Initial state
        PROCESSING,     // Payment is being processed
        ACCOUNT_UPDATED, // Account has been updated
        COMPLETED,      // Payment completed successfully
        FAILED,         // Payment failed
        CANCELED        // Payment canceled due to compensation
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Explicit getter for id (needed by PaymentWorker)
    public Long getId() {
        return id;
    }

    // Explicit getters for PaymentService
    public String getOrderId() {
        return orderId;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    // Explicit setter for id (needed by PaymentService)
    public void setId(Long id) {
        this.id = id;
    }

    // Explicit setters for PaymentWorker
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Saga-related methods
    public void initiateSaga(UUID transactionId) {
        this.transactionId = transactionId;
        this.sagaStatus = SagaStatus.PENDING;
    }

    public void startProcessing() {
        this.sagaStatus = SagaStatus.PROCESSING;
    }

    public void accountUpdated() {
        this.sagaStatus = SagaStatus.ACCOUNT_UPDATED;
    }

    public void completeSaga() {
        this.sagaStatus = SagaStatus.COMPLETED;
    }

    public void failSaga(String reason) {
        this.sagaStatus = SagaStatus.FAILED;
        this.failureReason = reason;
    }

    public void cancelSaga(String reason) {
        this.sagaStatus = SagaStatus.CANCELED;
        this.failureReason = reason;
    }

    // Getter for sagaStatus
    public SagaStatus getSagaStatus() {
        return sagaStatus;
    }

    // Setter for sagaStatus
    public void setSagaStatus(SagaStatus sagaStatus) {
        this.sagaStatus = sagaStatus;
    }

    // Các trường khác (ví dụ: thông tin người dùng, thời gian tạo)
}
