package com.example.payment_worker.repository;

import com.example.payment_worker.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

    /**
     * Find all pending outbox messages ordered by timestamp
     */
    List<OutboxMessage> findByStatusOrderByTimestampAsc(OutboxMessage.Status status);

    /**
     * Find pending messages older than specified time (for retry mechanism)
     */
    @Query("SELECT o FROM OutboxMessage o WHERE o.status = :status AND o.timestamp < :beforeTime ORDER BY o.timestamp ASC")
    List<OutboxMessage> findPendingMessagesOlderThan(
        @Param("status") OutboxMessage.Status status,
        @Param("beforeTime") LocalDateTime beforeTime
    );

    /**
     * Count messages by status
     */
    long countByStatus(OutboxMessage.Status status);

    /**
     * Find messages by aggregate type and id
     */
    List<OutboxMessage> findByAggregateTypeAndAggregateIdOrderByTimestampAsc(
        String aggregateType,
        String aggregateId
    );
}
