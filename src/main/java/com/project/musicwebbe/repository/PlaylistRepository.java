package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Playlist;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Page<Playlist> findAllByPlaylistNameContainingAndPlaylistStatusIsFalse(String playlistName, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_song WHERE playlist_id = :playlistId", nativeQuery = true)
    void deletePlaylistSongs(@Param("playlistId") Long playlistId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlists WHERE playlist_id = :playlistId", nativeQuery = true)
    void deletePlaylist(@Param("playlistId") Long playlistId);


    @Query("SELECT p from Playlist p JOIN p.favoritePlaylists f " +
            "WHERE f.appUser.userId = :userId ")
    Page<Playlist> findAllFavoritePlayListsByUserId(@Param("userId")Long userId, Pageable pageable);
}