package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.UserListen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserListenRepository extends JpaRepository<UserListen, Long> {

    UserListen findBySongSongIdAndAppUserUserIdAndListenedAt(Long songId, Long userId, LocalDate listenedAt);

    @Query(value = """
        SELECT * 
        FROM user_listens 
        WHERE user_id = :userId 
        ORDER BY listened_at DESC 
        """, nativeQuery = true)
    List<UserListen> findTop10RecentByUserId(@Param("userId") Long userId);
}
