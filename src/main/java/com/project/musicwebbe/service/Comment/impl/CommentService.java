package com.project.musicwebbe.service.Comment.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.service.Comment.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Page<Comment> searchAllBySongId(Long songId, Pageable pageable) {
        return commentRepository.searchAllBySongSongId(songId, pageable);
    }

    @Override
    public Page<Comment> findByParentCommentId(Long parentCommentId, Pageable pageable) {
        return commentRepository.findByParentCommentCommentId(parentCommentId, pageable);
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void remove(Long id) {
        commentRepository.deleteById(id);
    }

}
