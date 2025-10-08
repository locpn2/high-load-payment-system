package com.example.payment_worker.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessedEvent {
    private UUID transactionId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private Long paymentId;
}
