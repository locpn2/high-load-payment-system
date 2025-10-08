package com.example.payment_worker.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private UUID transactionId;
    private String orderId;
    private Long paymentId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
}
