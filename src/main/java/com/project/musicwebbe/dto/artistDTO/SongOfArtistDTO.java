package com.project.musicwebbe.dto.artistDTO;

import com.project.musicwebbe.dto.songDTO.AlbumOfSongDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import com.project.musicwebbe.entities.Genre;
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
public class SongOfArtistDTO {
    private Long songId;

    private String title;

    private LocalDateTime dateCreate;

    private String lyrics;

    private String songUrl;

    private int duration;

    private String coverImageUrl;

    List<ArtistOfSongDTO> artists;
}
