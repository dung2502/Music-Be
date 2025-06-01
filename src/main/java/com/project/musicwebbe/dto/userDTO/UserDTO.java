package com.project.musicwebbe.dto.userDTO;

import com.project.musicwebbe.entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;

    private String email;

    private String userCode;

    private LocalDateTime dateCreate;

    private String fullName;

    private Integer gender;

    private LocalDate dateOfBirth;

    private String avatar;

    private String phoneNumber;

    private String address;

    private LocalDateTime vipExpiredDate;

    private Boolean isVip = false;

}
