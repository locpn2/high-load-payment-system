package com.example.payment_worker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    // Các trường khác (ví dụ: thông tin người dùng, thời gian tạo)
}
