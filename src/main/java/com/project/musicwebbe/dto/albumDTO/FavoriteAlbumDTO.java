package com.project.musicwebbe.dto.albumDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteAlbumDTO {
    private Long favoriteAlbumId;

    private UserOfFavoriteAlbumDTO appUser;

    private LocalDateTime addedAt;
}
