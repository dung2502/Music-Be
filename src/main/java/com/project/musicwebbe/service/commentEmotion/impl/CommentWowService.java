package com.project.musicwebbe.service.commentEmotion.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.commentEmotion.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.repository.commentEmotion.*;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.commentEmotion.ICommentWowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentWowService implements ICommentWowService {
    @Autowired
    private CommentWowRepository commentWowRepository;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private CommentHahaRepository commentHahaRepository;

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
    public List<CommentWow> findAll() {
        return commentWowRepository.findAll();
    }

    @Override
    public Page<CommentWow> findAll(Pageable pageable) {
        return commentWowRepository.findAll(pageable);
    }

    @Override
    public CommentWow findById(Long id) {
        return commentWowRepository.findById(id).orElse(null);
    }

    @Override
    public void save(CommentWow commentWow) {
        commentWowRepository.save(commentWow);
    }

    @Override
    public void remove(Long id) {
        commentWowRepository.deleteById(id);
    }

    @Override
    public void wowComment(Long commentId, Long userId) {
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
                    createCommentWow(comment, user);
                }
                // Nếu emotion hiện tại là CommentDislike
                case CommentDislike dislike -> {
                    commentDislikeRepository.delete(dislike);
                    createCommentWow(comment, user);
                }
                // Nếu emotion hiện tại là CommentWow
                case CommentHaha commentHaha -> {
                    commentHahaRepository.delete(commentHaha);
                    createCommentWow(comment, user);
                }
                // Nếu emotion hiện tại là CommentHeart
                case CommentHeart commentHeart -> {
                    commentHeartRepository.delete(commentHeart);
                    createCommentWow(comment, user);
                }
                default -> {
                }
            }
        } else {
            createCommentWow(comment, user);
        }
    }

    private void createCommentWow(Comment comment, AppUser user) {
        CommentWow wow = new CommentWow();
        wow.setComment(comment);
        wow.setUser(user);
        wow.setCreatedAt(LocalDateTime.now());

        commentWowRepository.save(wow);
    }
}
