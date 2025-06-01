package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> searchAllByTitleContaining(String title);

    @Query(value = "SELECT s.song_id, s.title, s.date_create, s.lyrics, s.album_id, s.song_url, s.duration, s.cover_image_url " +
            "FROM songs s JOIN artist_song ars ON s.song_id = ars.song_id " +
            "JOIN artists a ON ars.artist_id = a.artist_id " +
            "WHERE s.title LIKE %:title% OR a.artist_name LIKE %:artistName% " +
            "GROUP BY s.song_id", nativeQuery = true)
    Page<Song> findAllByTitleAndArtist(@Param("title") String title, @Param("artistName") String artistName, Pageable pageable);

    @Query("SELECT s FROM Song s  JOIN s.songListens sl " +
            "WHERE sl.dateCreate >= :sevenDays " +
            "GROUP BY s.songId " +
            "ORDER BY SUM(sl.total) DESC LIMIT 3")
    List<Song> findTopThreeSongsInSevenDays(@Param("sevenDays") LocalDate sevenDays);


    @Query("SELECT s FROM Song s JOIN s.genres g JOIN s.songListens sl " +
            "WHERE g.genreName LIKE %:national% AND sl.dateCreate >= :startOfWeek " +
            "GROUP BY s.songId " +
            "ORDER BY SUM(sl.total) DESC")
    Page<Song> findAllTopSongsByNational(@Param("national")String national,
                                         @Param("startOfWeek") LocalDate startOfWeek,
                                         Pageable pageable);

    @Query("SELECT s from Song s JOIN s.favorites f " +
            "WHERE f.appUser.userId = :userId ")
    Page<Song> findAllFavoriteSongsByUserId(@Param("userId")Long userId, Pageable pageable);

    @Query("SELECT s, SUM(sl.total) AS total_listens FROM Song s JOIN s.songListens sl " +
            "GROUP BY s.songId " +
            "ORDER BY total_listens DESC, s.dateCreate DESC LIMIT 100")
    List<Song> findNewSongRatings();

    @Query("SELECT s FROM Song s JOIN s.genres g " +
            "WHERE g.genreName LIKE %:national% " +
            "GROUP BY s.songId " +
            "ORDER BY s.dateCreate DESC LIMIT 100")
    List<Song> findNewSongsWithNational(@Param("national")String national);

    @Query("SELECT s, SUM(sl.total) AS total_listens FROM Song s JOIN s.songListens sl " +
            "GROUP BY s.songId " +
            "ORDER BY total_listens DESC LIMIT 100")
    List<Song> findTop100Songs();

    @Query("""
SELECT s FROM Song s 
    LEFT JOIN s.songListens sl 
    GROUP BY s 
    ORDER BY SUM(sl.total) DESC
""")
    Page<Song> findSixSongBestListening(Pageable pageable);

    @Query("""
    SELECT s FROM Song s 
    WHERE EXISTS (
        SELECT 1 FROM UserListen ul 
        WHERE ul.song = s AND ul.appUser.userId = :userId
    )
    OR EXISTS (
        SELECT 1 FROM UserListen sl
        JOIN sl.song.genres g
        WHERE sl.appUser.userId = :userId
        AND g MEMBER OF s.genres
    )
""")
    Page<Song> findSuggestSongsByUserId(@Param("userId") Long userId, Pageable pageable);


    @Query("SELECT s FROM Song s ORDER BY s.dateCreate DESC ")
    Page<Song> findSongNewReleased(Pageable pageable);

}