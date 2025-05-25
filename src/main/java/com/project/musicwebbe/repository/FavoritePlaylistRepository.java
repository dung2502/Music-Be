package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.FavoritePlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritePlaylistRepository extends JpaRepository<FavoritePlaylist, Long> {

    void deleteByPlaylistPlaylistIdAndAppUserUserCodeContaining(Long playlistId, String userCode);

}