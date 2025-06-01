package com.project.musicwebbe.controller.auth.VNPAY;

import com.project.musicwebbe.config.VNPAYConfig;
import com.project.musicwebbe.dto.paymentDTO.PaymentDTO;
import com.project.musicwebbe.dto.paymentDTO.VnPayRequestDTO;
import com.project.musicwebbe.entities.Payment;
import com.project.musicwebbe.entities.VipPackage;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.payment.IPaymentService;
import com.project.musicwebbe.util.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceCallForVNPAY {

    private final VNPAYConfig vnPayConfig;
    private final IPaymentService paymentService;

    public PaymentDTO.VNPayResponse createVnPayPayment(VnPayRequestDTO requestDto) {
        long amount = requestDto.getAmount() * 100L;
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

        if (requestDto.getBankCode() != null && !requestDto.getBankCode().isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", requestDto.getBankCode());
        }

        vnpParamsMap.put("vnp_IpAddr", requestDto.getIpAddress());
        String transactionCode = VNPayUtil.getRandomNumber(10);
        vnpParamsMap.put("vnp_TxnRef", transactionCode);

        // 3. Tạo và lưu bản ghi Payment
        Payment payment = Payment.builder()
                .appUser(AppUser.builder().userId(requestDto.getUserId()).build()) // chỉ cần ID
                .vipPackage(VipPackage.builder().packageId(requestDto.getVipPackageId()).build()) // chỉ cần ID
                .amount(requestDto.getAmount())
                .paymentTime(LocalDateTime.now())
                .paymentMethod("VNPAY")
                .transactionCode(transactionCode)
                .paymentStatus("PENDING")
                .description("Đang chờ thanh toán")
                .build();
        paymentService.save(payment); // Lưu bản ghi vào DB

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        // 5. Trả về phản hồi
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }
}
