package com.project.musicwebbe.dto.paymentDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VnPayRequestDTO {
    private Long userId;
    private Long vipPackageId;
    private Long amount;     // đơn vị VND
    private String bankCode;
    private String ipAddress;
}
