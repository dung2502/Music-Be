package com.project.musicwebbe.dto.songDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOfFavoriteDTO {
    private Long userId;

    private String email;

    private String fullName;

    private String userCode;
}
