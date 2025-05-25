package com.project.musicwebbe.repository.commentEmotion;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.permission.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.musicwebbe.entities.commentEmotion.CommentDislike;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentDislikeRepository extends JpaRepository<CommentDislike, Long> {
    boolean existsByEmotionId(Long emotionId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment_dislike WHERE emotion_id = :emotionId", nativeQuery = true)
    void deleteByEmotion(@Param("emotionId") Long emotionId);
}