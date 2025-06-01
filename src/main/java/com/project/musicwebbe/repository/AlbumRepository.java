package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.entities.Song;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT a FROM Album a JOIN FETCH a.artists WHERE a.albumId = :albumId")
    Album findByIdWithArtists(@Param("albumId") Long albumId);

    List<Album> searchAllByTitleContaining(String title);

    @Query(value = "SELECT a.album_id, a.title, a.date_create, a.cover_image_url, a.provide, a.album_status " +
            "FROM albums a " +
            "JOIN artist_album aa ON a.album_id = aa.album_id " +
            "JOIN artists ar ON ar.artist_id = aa.artist_id " +
            "WHERE (a.title LIKE %:title% OR ar.artist_name LIKE %:artistName%) AND a.album_status = false",
            nativeQuery = true)
    Page<Album> searchAllByTitleAndArtistNameAndAlbumStatusIsFalse(@Param("title") String title,
                                                                   @Param("artistName") String artistName,
                                                                   Pageable pageable);


    @Query("SELECT a FROM Album a JOIN a.genres g " +
            "WHERE g.genreName LIKE %:national% " +
            "GROUP BY a.albumId " +
            "ORDER BY a.dateCreate DESC LIMIT 100")
    List<Album> findNewAlbumsWithNational(@Param("national")String national);

    @Query("SELECT a from Album a JOIN a.favoriteAlbums f " +
            "WHERE f.appUser.userId = :userId ")
    Page<Album> findAllFavoriteAlbumsByUserId(@Param("userId")Long userId, Pageable pageable);


    @Query("SELECT a FROM Album a " +
            "WHERE a.albumStatus = false " +
            "ORDER BY (SELECT SUM(sl.total) FROM Song s JOIN s.songListens sl WHERE s.album = a) DESC")
    Page<Album> findSixAlbumIsBestListenAndAlbumStatusIsFalse(Pageable pageable);




}