package com.project.musicwebbe.service.payment;

import com.project.musicwebbe.entities.Payment;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPaymentService extends IGeneralService<Payment> {
    Payment findByTransactionCode(String transactionCode);
    Page<Payment> getPaymentByUserId(Long userId, Pageable pageable);

    Page<Payment> getAllPaymentWithPage(Pageable pageable);

}