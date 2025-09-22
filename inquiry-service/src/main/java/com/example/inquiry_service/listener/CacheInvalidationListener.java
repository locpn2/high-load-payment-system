package com.example.inquiry_service.listener;

import com.example.inquiry_service.service.InquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener để xử lý các tin nhắn vô hiệu hóa cache từ Payment Worker.
 * Khi nhận được tin nhắn, listener sẽ sử dụng InquiryService để xóa cache.
 */
@Component
public class CacheInvalidationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheInvalidationListener.class);

    @Autowired
    private InquiryService inquiryService;

    /**
     * Lắng nghe tin nhắn từ topic "payment-updates" để vô hiệu hóa cache.
     *
     * @param orderId ID của order đã được cập nhật
     */
    @KafkaListener(topics = "payment-updates", groupId = "inquiry-service-cache-group")
    public void handleCacheInvalidation(String orderId) {
        LOGGER.info("Received cache invalidation message for order: {}", orderId);

        try {
            // Sử dụng InquiryService để vô hiệu hóa cache
            inquiryService.invalidateCache(orderId);
            LOGGER.info("Successfully processed cache invalidation for order: {}", orderId);

        } catch (Exception e) {
            LOGGER.error("Error processing cache invalidation for order {}: {}", orderId, e.getMessage(), e);
        }
    }
}
