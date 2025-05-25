package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.FavoriteAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteAlbumRepository extends JpaRepository<FavoriteAlbum, Long> {

    void deleteByAlbumAlbumIdAndAppUserUserCodeContaining(Long albumId, String userCode);
}