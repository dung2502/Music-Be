package com.project.musicwebbe.dto.playlistDTO;

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
public class PlaylistDTO {

    private Long playlistId;

    private String playlistName;

    private String playlistCode;

    private String coverImageUrl; // URL tới ảnh bìa playlist

    private LocalDateTime dateCreate;

    private boolean playlistStatus = false;

    private String description;

    private List<SongOfPlaylistDTO> songOfPlaylist;

    private UserOfPlaylistDTO appUser;

    private List<FavoritePlaylistDTO> favorites;

    private boolean userFavoriteStatus;
}
