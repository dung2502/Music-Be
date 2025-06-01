package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.song.songId = :songId AND c.parentComment IS NULL AND c.isDeleted = false ORDER BY c.commentId DESC")
    Page<Comment> searchAllBySongSongId(@Param("songId") Long songId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.commentId = :parentId AND c.isDeleted = false ORDER BY c.commentId ASC")
    Page<Comment> findByParentCommentId(@Param("parentId") Long parentId, Pageable pageable);

}
