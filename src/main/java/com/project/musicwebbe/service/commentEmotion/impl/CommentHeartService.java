package com.project.musicwebbe.service.commentEmotion.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.commentEmotion.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.repository.commentEmotion.*;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.commentEmotion.ICommentHeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentHeartService implements ICommentHeartService {

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private CommentHahaRepository commentHahaRepository;

    @Autowired
    private CommentWowRepository commentWowRepository;

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
    public List<CommentHeart> findAll() {
        return commentHeartRepository.findAll();
    }

    @Override
    public Page<CommentHeart> findAll(Pageable pageable) {
        return commentHeartRepository.findAll(pageable);
    }

    @Override
    public CommentHeart findById(Long id) {
        return commentHeartRepository.findById(id).orElse(null);
    }

    @Override
    public void save(CommentHeart commentHeart) {
        commentHeartRepository.save(commentHeart);
    }

    @Override
    public void remove(Long id) {
        commentHeartRepository.deleteById(id);
    }

    @Override
    public void heartComment(Long commentId, Long userId) {
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
                    createCommentHeart(comment, user);
                }
                // Nếu emotion hiện tại là CommentDislike
                case CommentDislike dislike -> {
                    commentDislikeRepository.delete(dislike);
                    createCommentHeart(comment, user);
                }
                // Nếu emotion hiện tại là CommentWow
                case CommentWow commentWow -> {
                    commentWowRepository.delete(commentWow);
                    createCommentHeart(comment, user);
                }
                // Nếu emotion hiện tại là CommentHeart
                case CommentHaha commentHaha -> {
                    commentHahaRepository.delete(commentHaha);
                    createCommentHeart(comment, user);
                }
                default -> {
                }
            }
        } else {
            createCommentHeart(comment, user);
        }
    }

    private void createCommentHeart(Comment comment, AppUser user) {
        CommentHeart heart = new CommentHeart();
        heart.setComment(comment);
        heart.setUser(user);
        heart.setCreatedAt(LocalDateTime.now());

        commentHeartRepository.save(heart);
    }
}
