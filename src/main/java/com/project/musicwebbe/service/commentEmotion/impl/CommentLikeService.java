package com.project.musicwebbe.service.commentEmotion.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.commentEmotion.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.repository.commentEmotion.*;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.commentEmotion.ICommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentLikeService implements ICommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentDislikeRepository commentDislikeRepository;

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
    public List<CommentLike> findAll() {
        return commentLikeRepository.findAll();
    }

    @Override
    public Page<CommentLike> findAll(Pageable pageable) {
        return commentLikeRepository.findAll(pageable);
    }

    @Override
    public CommentLike findById(Long id) {
        return commentLikeRepository.findById(id).orElse(null);
    }

    @Override
    public void save(CommentLike commentLike) {
        commentLikeRepository.save(commentLike);
    }

    @Override
    public void remove(Long likeId) {
        commentLikeRepository.deleteById(likeId);
    }

    @Override
    public void likeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommentEmotion existingEmotion = commentEmotionRepository.findByCommentAndUser(comment, user);
        if (existingEmotion != null) {
            // Nếu emotion hiện tại là CommentLike
            switch (existingEmotion) {
                case CommentDislike commentDislike -> {
                    commentDislikeRepository.delete(commentDislike);
                    createCommentLike(comment, user);
                }
                // Nếu emotion hiện tại là CommentDislike
                case CommentHaha haha -> {
                    commentHahaRepository.delete(haha);
                    createCommentLike(comment, user);
                }
                // Nếu emotion hiện tại là CommentWow
                case CommentWow commentWow -> {
                    commentWowRepository.delete(commentWow);
                    createCommentLike(comment, user);
                }
                // Nếu emotion hiện tại là CommentHeart
                case CommentHeart commentHeart -> {
                    commentHeartRepository.delete(commentHeart);
                    createCommentLike(comment, user);
                }
                default -> {
                }
            }
        } else {
            createCommentLike(comment, user);
        }
    }

    private void createCommentLike(Comment comment, AppUser user) {
        CommentLike like = new CommentLike();
        like.setComment(comment);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());

        commentLikeRepository.save(like);
    }
}
