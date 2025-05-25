package com.project.musicwebbe.service.commentEmotion.impl;

import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.entities.commentEmotion.CommentEmotion;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.CommentRepository;
import com.project.musicwebbe.repository.commentEmotion.CommentDislikeRepository;
import com.project.musicwebbe.repository.commentEmotion.CommentEmotionRepository;
import com.project.musicwebbe.repository.commentEmotion.CommentLikeRepository;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.commentEmotion.ICommentEmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentEmotionService implements ICommentEmotionService {

    @Autowired
    private CommentEmotionRepository commentEmotionRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentDislikeRepository commentDislikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CommentEmotion> findAll() {
        return commentEmotionRepository.findAll();
    }

    @Override
    public Page<CommentEmotion> findAll(Pageable pageable) {
        return commentEmotionRepository.findAll(pageable);
    }

    @Override
    public CommentEmotion findById(Long id) {
        return commentEmotionRepository.findById(id).orElse(null);
    }

    @Override
    public void save(CommentEmotion commentEmotion) {
        commentEmotionRepository.save(commentEmotion);
    }

    @Override
    public void remove(Long id) {
        commentEmotionRepository.deleteById(id);
    }

    @Override
    public void removeEmotion(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra nếu user đã thích bình luận trước đó
        CommentEmotion alreadyEmotion = commentEmotionRepository.findByCommentAndUser(comment, user);
        System.out.println(alreadyEmotion);
        if (alreadyEmotion != null) {
            commentLikeRepository.deleteByEmotion(alreadyEmotion.getEmotionId());
            commentDislikeRepository.deleteByEmotion(alreadyEmotion.getEmotionId());
            commentEmotionRepository.deleteByEmotion(alreadyEmotion.getEmotionId());
        }
    }
}