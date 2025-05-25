package com.project.musicwebbe.dto.albumDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOfFavoriteAlbumDTO {
    private Long userId;

    private String email;

    private String fullName;

    private String userCode;
}
