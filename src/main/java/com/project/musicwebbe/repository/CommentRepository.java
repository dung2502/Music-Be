package com.project.musicwebbe.repository;

import com.project.musicwebbe.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> searchAllBySongSongId(Long songId, Pageable pageable);

    Page<Comment> findByParentCommentCommentId(Long parentCommentId, Pageable pageable);
}
