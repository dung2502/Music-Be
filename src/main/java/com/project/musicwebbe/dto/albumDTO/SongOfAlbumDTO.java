package com.project.musicwebbe.dto.albumDTO;

import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
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
public class SongOfAlbumDTO {
    private Long songId;

    private String title;

    private LocalDateTime dateCreate;

    private String lyrics;

    private String songUrl;

    private int duration;

    private String coverImageUrl;

    private List<ArtistOfSongDTO> artists;

}
