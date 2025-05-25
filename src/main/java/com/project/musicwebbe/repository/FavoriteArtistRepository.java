package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.FavoriteArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {

    void deleteByArtistArtistIdAndAppUserUserCodeContaining(Long artistId, String userCode);

}