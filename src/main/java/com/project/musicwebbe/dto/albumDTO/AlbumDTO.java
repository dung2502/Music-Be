package com.project.musicwebbe.dto.albumDTO;

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
public class AlbumDTO {
    private Long albumId;

    private String title;

    private LocalDateTime dateCreate;

    private String coverImageUrl;

    private List<SongOfAlbumDTO> songs;

    private String provide;

    private List<ArtistOfAlbumDTO> artists;

    private List<Genre> genres;

    private List<FavoriteAlbumDTO> favorites;

    private boolean userFavoriteStatus;

}
