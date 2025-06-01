package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.paymentDTO.PaymentDTO;
import com.project.musicwebbe.dto.paymentDTO.VnPayRequestDTO;
import com.project.musicwebbe.entities.Payment;
import com.project.musicwebbe.entities.VipPackage;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.response.ResponseObject;
import com.project.musicwebbe.controller.auth.VNPAY.PaymentServiceCallForVNPAY;
import com.project.musicwebbe.service.email.EmailService;
import com.project.musicwebbe.service.payment.impl.PaymentService;
import com.project.musicwebbe.service.permission.IUserService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import com.project.musicwebbe.util.VNPayUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/auth/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceCallForVNPAY paymentServiceVNPAY;

    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @Autowired
    private final EmailService emailService;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        VnPayRequestDTO requestDTO = VnPayRequestDTO.builder()
                .userId(Long.parseLong(request.getParameter("userId")))
                .vipPackageId(Long.parseLong(request.getParameter("vipPackageId")))
                .amount(Long.parseLong(request.getParameter("amount")))
                .bankCode(request.getParameter("bankCode"))
                .ipAddress(VNPayUtil.getIpAddress(request))
                .build();

        return new ResponseObject<>(HttpStatus.OK, "Success", paymentServiceVNPAY.createVnPayPayment(requestDTO));
    }

    @GetMapping("/vn-pay-callback")
    public RedirectView payCallbackHandler(@RequestParam Map<String, String> params) {
        String transactionCode = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");

        Payment payment = paymentService.findByTransactionCode(transactionCode);
        if (payment == null) {
            return new RedirectView("http://localhost:3000/payment/result?status=not_found");
        }

        if ("00".equals(responseCode)) {
            payment.setPaymentStatus("RESOLVED");
            payment.setDescription("Đã thanh toán");
            paymentService.save(payment);

            AppUser appUser = payment.getAppUser();
            VipPackage vipPackage = payment.getVipPackage();

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime baseTime = appUser.getVipExpiredDate() != null && appUser.getVipExpiredDate().isAfter(currentTime)
                    ? appUser.getVipExpiredDate()
                    : currentTime;

            Integer durationMonths = vipPackage.getDurationMonths();
            LocalDateTime newExpiredDate = baseTime.plusMonths(durationMonths != null ? durationMonths : 1);

            appUser.setIsVip(true);
            appUser.setVipExpiredDate(newExpiredDate);
            userService.save(appUser);
            try {
                emailService.sendPaymentSuccessEmail(
                        appUser.getEmail(),
                        appUser.getFullName(),
                        vipPackage.getName(),
                        newExpiredDate
                );
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }

            return new RedirectView("http://localhost:3000/payment/result?status=success");
        } else {
            payment.setPaymentStatus("FAILED");
            payment.setDescription("Thanh toán thất bại hoặc bị hủy");
            paymentService.save(payment);
            return new RedirectView("http://localhost:3000/payment/result?status=fail");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<PaymentDTO>> getByUserId(@PathVariable Long userId, @RequestParam(value = "page", defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        Page<Payment> payments = paymentService.getPaymentByUserId(userId, PageRequest.of(page, 5));
        Page<PaymentDTO> dtoList = payments.map(convertEntityToDTO::convertToPaymentDTO);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<PaymentDTO>> getAllWithPage(@RequestParam(value = "page", defaultValue = "0") int page)
    {
        if (page < 0) {
            page = 0;
        }
        Page<Payment> payments = paymentService.getAllPaymentWithPage(PageRequest.of(page, 10));

        Page<PaymentDTO> dtoList = payments.map(convertEntityToDTO::convertToPaymentDTO);
        return ResponseEntity.ok(dtoList);
    }
}


