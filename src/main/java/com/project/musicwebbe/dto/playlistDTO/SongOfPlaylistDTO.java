package com.project.musicwebbe.dto.playlistDTO;


import com.project.musicwebbe.dto.songDTO.AlbumOfSongDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import com.project.musicwebbe.dto.songDTO.SongDTO;
import com.project.musicwebbe.entities.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SongOfPlaylistDTO {

    private Long id;

    private Long songId;

    private LocalDateTime dateAdd;

    private String title;

    private LocalDateTime dateCreate;

    private String lyrics;

    private AlbumOfSongDTO album;

    private String songUrl;

    private int duration;

    private String coverImageUrl;

    private List<Genre> genres;

    private List<ArtistOfSongDTO> artists;


}
