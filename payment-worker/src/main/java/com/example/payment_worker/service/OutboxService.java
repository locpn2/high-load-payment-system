package com.example.payment_worker.service;

import com.example.payment_worker.entity.OutboxMessage;
import com.example.payment_worker.repository.OutboxMessageRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboxService.class);

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    private final Gson gson;

    public OutboxService() {
        this.gson = new Gson();
    }

    /**
     * Save an event to the outbox table within the same transaction
     */
    @Transactional
    public void saveEvent(String aggregateType, String aggregateId, String eventType, Object event) {
        try {
            String payload = gson.toJson(event);
            OutboxMessage outboxMessage = new OutboxMessage(aggregateType, aggregateId, eventType, payload);
            outboxMessageRepository.save(outboxMessage);
            LOGGER.debug("Saved event to outbox: {} for aggregate {}/{}", eventType, aggregateType, aggregateId);
        } catch (Exception e) {
            LOGGER.error("Failed to save event to outbox: {} for aggregate {}/{}", eventType, aggregateType, aggregateId, e);
            throw new RuntimeException("Failed to save event to outbox", e);
        }
    }

    /**
     * Mark an outbox message as processed
     */
    @Transactional
    public void markAsProcessed(Long outboxMessageId) {
        try {
            OutboxMessage message = outboxMessageRepository.findById(outboxMessageId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox message not found: " + outboxMessageId));
            message.markAsProcessed();
            outboxMessageRepository.save(message);
            LOGGER.debug("Marked outbox message as processed: {}", outboxMessageId);
        } catch (Exception e) {
            LOGGER.error("Failed to mark outbox message as processed: {}", outboxMessageId, e);
            throw new RuntimeException("Failed to mark outbox message as processed", e);
        }
    }

    /**
     * Mark an outbox message as failed
     */
    @Transactional
    public void markAsFailed(Long outboxMessageId) {
        try {
            OutboxMessage message = outboxMessageRepository.findById(outboxMessageId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox message not found: " + outboxMessageId));
            message.markAsFailed();
            outboxMessageRepository.save(message);
            LOGGER.debug("Marked outbox message as failed: {}", outboxMessageId);
        } catch (Exception e) {
            LOGGER.error("Failed to mark outbox message as failed: {}", outboxMessageId, e);
            throw new RuntimeException("Failed to mark outbox message as failed", e);
        }
    }
}
