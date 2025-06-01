package com.project.musicwebbe.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vip_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VipPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @Column(name = "name")
    private String name;

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(name = "price")
    private Long price;
}
