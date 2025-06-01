package com.project.musicwebbe.service.payment.impl;

import com.project.musicwebbe.entities.Payment;
import com.project.musicwebbe.repository.PaymentRepository;
import com.project.musicwebbe.service.payment.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public void remove(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Payment findByTransactionCode(String transactionCode) {
        return paymentRepository.findByTransactionCode(transactionCode).orElse(null);
    }

    @Override
    public Page<Payment> getPaymentByUserId(Long userId, Pageable pageable) {
        return paymentRepository.findPaymentsByUserId(userId, pageable);
    }

    @Override
    public Page<Payment> getAllPaymentWithPage(Pageable pageable) {
        return paymentRepository.findAllWithPage(pageable);
    }
}