package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByAppUser_UserId(Long userId);
    // search by title , lyric ,artist
    @Query("SELECT f FROM Favorite f " +
            "JOIN f.song s " +
            "JOIN s.artists a " +
            "WHERE f.appUser.userId = :userId " +
            "AND (s.title LIKE %:search% " +
            "OR s.lyrics LIKE %:search% " +
            "OR a.artistName LIKE %:search%)")
    List<Favorite> findAllByUserIdAndSearch(@Param("userId") Long userId, @Param("search") String search);

    void deleteBySongSongIdAndAppUserUserCodeContaining(Long songId, String userCode);

}