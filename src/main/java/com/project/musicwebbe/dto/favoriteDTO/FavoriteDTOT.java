package com.project.musicwebbe.dto.favoriteDTO;

import com.project.musicwebbe.dto.songDTO.SimpleUserDTO;
import com.project.musicwebbe.dto.songDTO.SongDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTOT {
    private Long favoriteId;

    private LocalDateTime addedAt;

    private SimpleUserDTO userFav;

    private SongDTO favoriteSong;


}
