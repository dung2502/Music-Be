package com.project.musicwebbe.service.Comment;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentService extends IGeneralService<Comment> {
    Page<Comment> searchAllBySongId(Long songId, Pageable pageable);

    Page<Comment> findByParentCommentId(Long parentCommentId, Pageable pageable);
}
