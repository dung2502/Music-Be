package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.dto.commentDTO.*;
import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.service.Comment.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/comments")
public class CommentRestController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("/song/{songId}")
    public ResponseEntity<Page<CommentDTO>> getAllCommentBySongId(@PathVariable Long songId,
                                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = commentService.searchAllBySongId(songId, pageable);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Page<CommentDTO> commentDTOS = comments.map(this::convertToCommentDTO);
        return ResponseEntity.ok(commentDTOS);
    }

    @GetMapping("/replies/{commentId}")
    public ResponseEntity<Page<CommentDTO>> getReplies(@PathVariable Long commentId,
                                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> repliesPage = commentService.findByParentCommentId(commentId, pageable);

        if (repliesPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Page<CommentDTO> repliesDTO = repliesPage.map(this::convertToCommentDTO);
        return ResponseEntity.ok(repliesDTO);
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        ;
        if (comment.getUser() != null) {
            commentDTO.setUser(UserOfCommentDTO.builder()
                    .userId(comment.getUser().getUserId())
                    .email(comment.getUser().getEmail())
                    .fullName(comment.getUser().getFullName())
                    .avatar(comment.getUser().getAvatar())
                    .userCode(comment.getUser().getUserCode())
                    .build());
        }

        if (comment.getSong() != null) {
            commentDTO.setSong(SongOfCommentDTO.builder()
                    .songId(comment.getSong().getSongId())
                    .songUrl(comment.getSong().getSongUrl())
                    .title(comment.getSong().getTitle())
                    .dateCreate(comment.getSong().getDateCreate())
                    .lyrics(comment.getSong().getLyrics())
                    .coverImageUrl(comment.getSong().getCoverImageUrl())
                    .duration(comment.getSong().getDuration())
                    .build());
        }

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")); // Số lượng replies trên mỗi trang
        Page<Comment> repliesPage = commentService.findByParentCommentId(comment.getCommentId(), pageable);

        if (repliesPage != null && !repliesPage.isEmpty()) {
            Page<CommentDTO> replies = repliesPage.map(this::convertToCommentDTO);
            commentDTO.setReplies(replies);
        }

        if (comment.getLikes() != null && !comment.getLikes().isEmpty()) {
            List<CommentLikeDTO> likes = comment.getLikes().stream()
                    .map(commentLike -> CommentLikeDTO.builder()
                            .likeId(commentLike.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentLike.getUser().getUserId())
                                    .email(commentLike.getUser().getEmail())
                                    .fullName(commentLike.getUser().getFullName())
                                    .avatar(commentLike.getUser().getAvatar())
                                    .userCode(commentLike.getUser().getUserCode())
                                    .build())
                            .likedAt(commentLike.getCreatedAt())
                            .build()).toList();
            commentDTO.setLikes(likes);
        }

        if (comment.getDislikes() != null && !comment.getDislikes().isEmpty()) {
            List<CommentDislikeDTO> dislikes = comment.getDislikes().stream()
                    .map(commentDislike -> CommentDislikeDTO.builder()
                            .dislikeId(commentDislike.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentDislike.getUser().getUserId())
                                    .email(commentDislike.getUser().getEmail())
                                    .fullName(commentDislike.getUser().getFullName())
                                    .avatar(commentDislike.getUser().getAvatar())
                                    .userCode(commentDislike.getUser().getUserCode())
                                    .build())
                            .dislikedAt(commentDislike.getCreatedAt())
                            .build()).toList();
            commentDTO.setDislikes(dislikes);
        }

        if (comment.getHahas() != null && !comment.getHahas().isEmpty()) {
            List<CommentHahaDTO> hahaDTOS = comment.getHahas().stream()
                    .map(commentHaha -> CommentHahaDTO.builder()
                            .hahaId(commentHaha.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentHaha.getUser().getUserId())
                                    .email(commentHaha.getUser().getEmail())
                                    .fullName(commentHaha.getUser().getFullName())
                                    .avatar(commentHaha.getUser().getAvatar())
                                    .userCode(commentHaha.getUser().getUserCode())
                                    .build())
                            .hahaAt(commentHaha.getCreatedAt())
                            .build()).toList();
            commentDTO.setHahas(hahaDTOS);
        }

        if (comment.getWows() != null && !comment.getWows().isEmpty()) {
            List<CommentWowDTO> wowDTOS = comment.getWows().stream()
                    .map(commentWow -> CommentWowDTO.builder()
                            .wowId(commentWow.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentWow.getUser().getUserId())
                                    .email(commentWow.getUser().getEmail())
                                    .fullName(commentWow.getUser().getFullName())
                                    .avatar(commentWow.getUser().getAvatar())
                                    .userCode(commentWow.getUser().getUserCode())
                                    .build())
                            .wowAt(commentWow.getCreatedAt())
                            .build()).toList();
            commentDTO.setWows(wowDTOS);
        }

        if (comment.getHearts() != null && !comment.getHearts().isEmpty()) {
            List<CommentHeartDTO> dislikes = comment.getHearts().stream()
                    .map(commentHeart -> CommentHeartDTO.builder()
                            .heartId(commentHeart.getEmotionId())
                            .user(UserOfCommentDTO.builder()
                                    .userId(commentHeart.getUser().getUserId())
                                    .email(commentHeart.getUser().getEmail())
                                    .fullName(commentHeart.getUser().getFullName())
                                    .avatar(commentHeart.getUser().getAvatar())
                                    .userCode(commentHeart.getUser().getUserCode())
                                    .build())
                            .heartedAt(commentHeart.getCreatedAt())
                            .build()).toList();
            commentDTO.setHearts(dislikes);
        }

        return commentDTO;
    }

}
