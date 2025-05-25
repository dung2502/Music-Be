package com.project.musicwebbe.repository.commentEmotion;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.permission.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.musicwebbe.entities.commentEmotion.CommentEmotion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentEmotionRepository extends JpaRepository<CommentEmotion, Long> {
    boolean existsByCommentAndUser(Comment comment, AppUser user);
    CommentEmotion findByCommentAndUser(Comment comment, AppUser user);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment_emotion WHERE emotion_id = :emotionId", nativeQuery = true)
    void deleteByEmotion(@Param("emotionId") Long emotionId);
}