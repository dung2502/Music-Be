package com.project.musicwebbe.dto.paymentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipPackageDTO {

    private Long packageId;

    private String name;

    private Integer durationMonths;

    private Long price;
}
