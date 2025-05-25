package com.project.musicwebbe.service.commentEmotion.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.commentEmotion.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.repository.commentEmotion.*;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.commentEmotion.ICommentHahaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentHahaService implements ICommentHahaService {
    @Autowired
    private CommentHahaRepository commentHahaRepository;

    @Autowired
    private CommentWowRepository commentWowRepository;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private CommentDislikeRepository commentDislikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentEmotionRepository commentEmotionRepository;

    @Override
    public List<CommentHaha> findAll() {
        return commentHahaRepository.findAll();
    }

    @Override
    public Page<CommentHaha> findAll(Pageable pageable) {
        return commentHahaRepository.findAll(pageable);
    }

    @Override
    public CommentHaha findById(Long id) {
        return commentHahaRepository.findById(id).orElse(null);
    }

    @Override
    public void save(CommentHaha commentHaha) {
        commentHahaRepository.save(commentHaha);
    }

    @Override
    public void remove(Long id) {
        commentHahaRepository.deleteById(id);
    }

    @Override
    public void hahaComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommentEmotion existingEmotion = commentEmotionRepository.findByCommentAndUser(comment, user);
        if (existingEmotion != null) {
            // Nếu emotion hiện tại là CommentLike
            switch (existingEmotion) {
                case CommentLike commentLike -> {
                    commentLikeRepository.delete(commentLike);
                    createCommentHaha(comment, user);
                }
                // Nếu emotion hiện tại là CommentDislike
                case CommentDislike dislike -> {
                    commentDislikeRepository.delete(dislike);
                    createCommentHaha(comment, user);
                }
                // Nếu emotion hiện tại là CommentWow
                case CommentWow commentWow -> {
                    commentWowRepository.delete(commentWow);
                    createCommentHaha(comment, user);
                }
                // Nếu emotion hiện tại là CommentHeart
                case CommentHeart commentHeart -> {
                    commentHeartRepository.delete(commentHeart);
                    createCommentHaha(comment, user);
                }
                default -> {
                }
            }
        } else {
            createCommentHaha(comment, user);
        }
    }

    private void createCommentHaha(Comment comment, AppUser user) {
        CommentHaha haha = new CommentHaha();
        haha.setComment(comment);
        haha.setUser(user);
        haha.setCreatedAt(LocalDateTime.now());

        commentHahaRepository.save(haha);
    }
}
