package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionCode(String transactionCode);

    @Query("SELECT p FROM Payment p WHERE p.appUser.userId = :userId")
    Page<Payment> findPaymentsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Payment p")
    Page<Payment> findAllWithPage(Pageable pageable);
}
