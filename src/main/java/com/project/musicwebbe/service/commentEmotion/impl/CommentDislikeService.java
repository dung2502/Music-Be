package com.project.musicwebbe.service.commentEmotion.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.commentEmotion.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.repository.commentEmotion.*;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.commentEmotion.ICommentDislikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentDislikeService implements ICommentDislikeService {

    @Autowired
    private CommentDislikeRepository commentDislikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentHahaRepository commentHahaRepository;

    @Autowired
    private CommentWowRepository commentWowRepository;

    @Autowired
    private CommentHeartRepository commentHeartRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentEmotionRepository commentEmotionRepository;

    @Override
    public List<CommentDislike> findAll() {
        return commentDislikeRepository.findAll();
    }

    @Override
    public Page<CommentDislike> findAll(Pageable pageable) {
        return commentDislikeRepository.findAll(pageable);
    }

    @Override
    public CommentDislike findById(Long id) {
        return commentDislikeRepository.findById(id).orElse(null);
    }

    @Override
    public void save(CommentDislike commentDislike) {
        commentDislikeRepository.save(commentDislike);
    }

    @Override
    public void remove(Long id) {
        commentDislikeRepository.deleteById(id);
    }

    @Override
    public void dislikeComment(Long commentId, Long userId) {
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
                    createCommentDislike(comment, user);
                }
                // Nếu emotion hiện tại là CommentDislike
                case CommentHaha haha -> {
                    commentHahaRepository.delete(haha);
                    createCommentDislike(comment, user);
                }
                // Nếu emotion hiện tại là CommentWow
                case CommentWow commentWow -> {
                    commentWowRepository.delete(commentWow);
                    createCommentDislike(comment, user);
                }
                // Nếu emotion hiện tại là CommentHeart
                case CommentHeart commentHeart -> {
                    commentHeartRepository.delete(commentHeart);
                    createCommentDislike(comment, user);
                }
                default -> {
                }
            }
        } else {
            createCommentDislike(comment, user);
        }
    }

    private void createCommentDislike(Comment comment, AppUser user) {
        CommentDislike dislike = new CommentDislike();
        dislike.setComment(comment);
        dislike.setUser(user);
        dislike.setCreatedAt(LocalDateTime.now());

        commentDislikeRepository.save(dislike);
    }
}
