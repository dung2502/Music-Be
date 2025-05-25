package com.project.musicwebbe.dto.artistDTO;

import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.entities.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private Long artistId;

    private String artistName;

    private String avatar;

    private String biography;

    private List<Genre> genres;

    private List<SongOfArtistDTO> songs;

    private List<AlbumOfArtistDTO> albums;

    private List<FavoriteArtistDTO> favorites;

    private boolean userFavoriteStatus;
}
