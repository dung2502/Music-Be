package com.project.musicwebbe.dto.artistDTO;

import com.project.musicwebbe.dto.songDTO.UserOfFavoriteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteArtistDTO {
    private Long favoriteArtistId;

    private UserOfFavoriteArtistDTO appUser;

    private LocalDateTime addedAt;
}
