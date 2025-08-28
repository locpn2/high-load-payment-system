package com.example.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
}
