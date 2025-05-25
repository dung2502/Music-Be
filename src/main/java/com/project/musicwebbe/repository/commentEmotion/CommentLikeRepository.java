package com.project.musicwebbe.repository.commentEmotion;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.musicwebbe.entities.commentEmotion.CommentLike;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByEmotionId(Long emotionId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment_like WHERE emotion_id = :emotionId", nativeQuery = true)
    void deleteByEmotion(@Param("emotionId") Long emotionId);
}