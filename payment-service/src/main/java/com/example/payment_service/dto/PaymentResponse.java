package com.example.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponse {

    private Long id;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String status;
}
