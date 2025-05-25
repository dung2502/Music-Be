package com.project.musicwebbe.service.playlist;

import com.project.musicwebbe.entities.Playlist;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPlaylistService extends IGeneralService<Playlist> {
    Page<Playlist> searchAllByPlaylistName(String playlistName, Pageable pageable);
    Page<Playlist> searchAllFavoritePlaylistsByUserId(Long userId, Pageable pageable);
    void deletePlaylistSongs(Long playlistId);
    void deletePlaylist(Long playlistId);

}
