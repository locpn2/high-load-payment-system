package com.example.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

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
}
