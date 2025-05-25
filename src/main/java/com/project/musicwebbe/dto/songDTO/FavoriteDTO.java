package com.project.musicwebbe.dto.songDTO;

import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.permission.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Long favoriteId;

    private UserOfFavoriteDTO appUser;

    private LocalDateTime addedAt;
}
