package com.example.inquiry_service.controller;

import com.example.inquiry_service.entity.Payment;
import com.example.inquiry_service.service.InquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InquiryController - REST API endpoints cho Inquiry Service
 *
 * Các endpoints:
 * - GET /api/inquiries/balance/{accountId} - Lấy số dư tài khoản
 * - GET /api/inquiries/transactions/{accountId} - Lấy lịch sử giao dịch
 */
@RestController
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    private static final Logger logger = LoggerFactory.getLogger(InquiryController.class);

    @Autowired
    private InquiryService inquiryService;

    /**
     * Lấy số dư tài khoản
     * @param accountId - ID tài khoản
     * @return ResponseEntity chứa số dư
     */
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String accountId) {
        logger.info("Received request to get balance for account: {}", accountId);

        try {
            BigDecimal balance = inquiryService.getBalance(accountId);

            Map<String, Object> response = new HashMap<>();
            response.put("accountId", accountId);
            response.put("balance", balance);
            response.put("currency", "USD"); // Có thể lấy từ database nếu cần
            response.put("timestamp", System.currentTimeMillis());

            logger.info("Successfully retrieved balance for account: {} - Balance: {}", accountId, balance);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting balance for account: {}", accountId, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve balance");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("accountId", accountId);
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Lấy lịch sử giao dịch
     * @param accountId - ID tài khoản
     * @return ResponseEntity chứa danh sách giao dịch
     */
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<Map<String, Object>> getTransactions(@PathVariable String accountId) {
        logger.info("Received request to get transactions for account: {}", accountId);

        try {
            List<Payment> transactions = inquiryService.getTransactions(accountId);

            Map<String, Object> response = new HashMap<>();
            response.put("accountId", accountId);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            response.put("timestamp", System.currentTimeMillis());

            logger.info("Successfully retrieved {} transactions for account: {}", transactions.size(), accountId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting transactions for account: {}", accountId, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve transactions");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("accountId", accountId);
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Health check endpoint
     * @return ResponseEntity với status OK
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "inquiry-service");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
