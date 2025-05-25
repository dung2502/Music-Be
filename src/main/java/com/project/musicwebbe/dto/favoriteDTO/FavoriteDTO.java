package com.project.musicwebbe.dto.favoriteDTO;

import com.project.musicwebbe.dto.songDTO.SongDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Long favoriteId;

    private SongDTO songDTO;

    private LocalDateTime addedAt;
}
