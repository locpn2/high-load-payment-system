package com.example.payment_worker.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent {
    private UUID transactionId;
    private String orderId;
    private Long paymentId;
    private String failureReason;
}
