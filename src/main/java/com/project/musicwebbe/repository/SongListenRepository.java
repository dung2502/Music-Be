package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.SongListen;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SongListenRepository extends JpaRepository<SongListen, Long> {
    SongListen findBySongSongIdAndDateCreate(Long songId, LocalDate date);

    @Query(value = " SELECT listen_id, date_create, total, song_id " +
        "FROM song_listens WHERE (date_create >= CURDATE() - INTERVAL 7 DAY) AND song_id = :songId " +
        "ORDER BY song_id, date_create ASC", nativeQuery = true)
    List<SongListen> findAllBySongIdInSevenDays(Long songId);
}
