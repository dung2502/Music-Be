package com.project.musicwebbe.dto.paymentDTO;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long paymentId;

    private Long userId;

    private String fullName;

    private Long amount;

    private LocalDateTime paymentTime;

    private String paymentMethod;

    private String transactionCode;

    private String paymentStatus;

    private String description;

    private VipPackageDTO vipPackage;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VNPayResponse {
        private String code;
        private String message;
        private String paymentUrl;
    }
}
