package com.project.musicwebbe.repository.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentHaha;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentHahaRepository extends JpaRepository<CommentHaha, Long> {
    boolean existsByEmotionId(Long emotionId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment_haha WHERE emotion_id = :emotionId", nativeQuery = true)
    void deleteByEmotion(@Param("emotionId") Long emotionId);
}
