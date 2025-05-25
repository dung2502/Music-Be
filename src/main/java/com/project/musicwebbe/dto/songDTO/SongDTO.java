package com.project.musicwebbe.dto.songDTO;

import com.project.musicwebbe.entities.Genre;
import com.project.musicwebbe.entities.SongListen;
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
public class SongDTO {
    private Long songId;

    private String title;

    private LocalDateTime dateCreate;

    private String lyrics;

    private AlbumOfSongDTO album;

    private String songUrl;

    private int duration;

    private String coverImageUrl;

    private List<Genre> genres;

    private List<SongListenDTO> songListens;

    private List<ArtistOfSongDTO> artists;

    private int listens;

    private List<FavoriteDTO> favorites;

    private boolean userFavoriteStatus;
}
