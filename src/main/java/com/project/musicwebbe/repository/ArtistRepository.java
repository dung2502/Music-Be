package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> searchAllByArtistNameContaining(String artistName);

    Page<Artist> searchAllByArtistNameContainingAndArtistStatusIsFalse(String artistName, Pageable pageable);

    Artist findByArtistNameContaining(String artistName);

    @Query("SELECT a from Artist a JOIN a.favoriteArtists f " +
            "WHERE f.appUser.userId = :userId ")
    Page<Artist> findAllFavoriteArtistsByUserId(@Param("userId")Long userId, Pageable pageable);
}