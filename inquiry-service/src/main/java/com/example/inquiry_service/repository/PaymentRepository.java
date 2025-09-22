package com.example.inquiry_service.repository;

import com.example.inquiry_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Tìm tất cả giao dịch theo accountId (orderId)
     * @param accountId - ID của tài khoản
     * @return Danh sách giao dịch
     */
    List<Payment> findByOrderId(String accountId);

    /**
     * Tính tổng số dư theo accountId
     * @param accountId - ID của tài khoản
     * @return Tổng số dư
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.orderId = :accountId AND p.status = 'COMPLETED'")
    BigDecimal getTotalBalanceByAccountId(@Param("accountId") String accountId);

    /**
     * Tìm giao dịch theo orderId và status
     * @param orderId - ID đơn hàng
     * @param status - Trạng thái giao dịch
     * @return Payment entity
     */
    Payment findByOrderIdAndStatus(String orderId, String status);
}
