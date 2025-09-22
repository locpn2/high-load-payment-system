package com.example.inquiry_service.service;

import com.example.inquiry_service.entity.Payment;
import com.example.inquiry_service.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * InquiryService - Triển khai Cache-Aside Pattern
 *
 * Logic hoạt động:
 * 1. Khi có yêu cầu, kiểm tra Redis cache trước
 * 2. Nếu cache miss, truy vấn từ database replica
 * 3. Lưu kết quả vào Redis cache trước khi trả về
 * 4. Cache sẽ tự động expire sau một khoảng thời gian
 */
@Service
public class InquiryService {

    private static final Logger logger = LoggerFactory.getLogger(InquiryService.class);

    private static final String BALANCE_CACHE_PREFIX = "balance:";
    private static final String TRANSACTIONS_CACHE_PREFIX = "transactions:";
    private static final int CACHE_TTL_MINUTES = 5; // Cache expire sau 5 phút

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Lấy số dư tài khoản - sử dụng Cache-Aside pattern
     * @param accountId - ID tài khoản
     * @return Số dư hiện tại
     */
    public BigDecimal getBalance(String accountId) {
        String cacheKey = BALANCE_CACHE_PREFIX + accountId;
        logger.info("Checking cache for balance of account: {}", accountId);

        try {
            // 1. Kiểm tra cache trước
            Object cachedBalance = redisTemplate.opsForValue().get(cacheKey);
            if (cachedBalance != null) {
                logger.info("Cache hit for balance of account: {}", accountId);
                return (BigDecimal) cachedBalance;
            }

            // 2. Cache miss - truy vấn database
            logger.info("Cache miss for balance of account: {}, querying database", accountId);
            BigDecimal balance = paymentRepository.getTotalBalanceByAccountId(accountId);

            // 3. Lưu vào cache trước khi trả về
            redisTemplate.opsForValue().set(cacheKey, balance, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            logger.info("Cached balance for account: {} with value: {}", accountId, balance);

            return balance;

        } catch (Exception e) {
            logger.error("Error getting balance for account: {}", accountId, e);
            throw new RuntimeException("Failed to get balance", e);
        }
    }

    /**
     * Lấy lịch sử giao dịch - sử dụng Cache-Aside pattern
     * @param accountId - ID tài khoản
     * @return Danh sách giao dịch
     */
    public List<Payment> getTransactions(String accountId) {
        String cacheKey = TRANSACTIONS_CACHE_PREFIX + accountId;
        logger.info("Checking cache for transactions of account: {}", accountId);

        try {
            // 1. Kiểm tra cache trước
            Object cachedTransactions = redisTemplate.opsForValue().get(cacheKey);
            if (cachedTransactions != null) {
                logger.info("Cache hit for transactions of account: {}", accountId);
                return (List<Payment>) cachedTransactions;
            }

            // 2. Cache miss - truy vấn database
            logger.info("Cache miss for transactions of account: {}, querying database", accountId);
            List<Payment> transactions = paymentRepository.findByOrderId(accountId);

            // 3. Lưu vào cache trước khi trả về
            redisTemplate.opsForValue().set(cacheKey, transactions, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            logger.info("Cached {} transactions for account: {}", transactions.size(), accountId);

            return transactions;

        } catch (Exception e) {
            logger.error("Error getting transactions for account: {}", accountId, e);
            throw new RuntimeException("Failed to get transactions", e);
        }
    }

    /**
     * Xóa cache khi có thay đổi dữ liệu (được gọi từ Payment Worker)
     * @param accountId - ID tài khoản cần invalidate cache
     */
    public void invalidateCache(String accountId) {
        String balanceCacheKey = BALANCE_CACHE_PREFIX + accountId;
        String transactionsCacheKey = TRANSACTIONS_CACHE_PREFIX + accountId;

        try {
            redisTemplate.delete(balanceCacheKey);
            redisTemplate.delete(transactionsCacheKey);
            logger.info("Invalidated cache for account: {}", accountId);
        } catch (Exception e) {
            logger.error("Error invalidating cache for account: {}", accountId, e);
        }
    }
}
