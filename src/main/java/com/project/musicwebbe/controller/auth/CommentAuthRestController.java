package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.commentDTO.*;
import com.project.musicwebbe.entities.Comment;
import com.project.musicwebbe.service.Comment.ICommentService;
import com.project.musicwebbe.service.commentEmotion.*;
import com.project.musicwebbe.service.permission.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth/comments")
public class CommentAuthRestController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentEmotionService commentEmotionService;

    @Autowired
    private ICommentLikeService commentLikeService;

    @Autowired
    private ICommentDislikeService commentDislikeService;

    @Autowired
    private ICommentHahaService commentHahaService;

    @Autowired
    private ICommentWowService commentWowService;

    @Autowired
    private ICommentHeartService commentHeartService;

    @Autowired
    private IUserService userService;

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

    @PostMapping("/user/{userId}")
    public ResponseEntity<Comment> addComment(@PathVariable(name = "userId")Long userId,
                                              @RequestBody Comment comment
    ) {
        var user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        commentService.save(comment);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId, @RequestParam(name = "userId") Long userId) {
        commentLikeService.likeComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable Long commentId, @RequestParam(name = "userId") Long userId) {
        commentDislikeService.dislikeComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/haha")
    public ResponseEntity<Void> hahaComment(@PathVariable Long commentId, @RequestParam(name = "userId") Long userId) {
        commentHahaService.hahaComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/wow")
    public ResponseEntity<Void> wowComment(@PathVariable Long commentId, @RequestParam(name = "userId") Long userId) {
        commentWowService.wowComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/heart")
    public ResponseEntity<Void> heartComment(@PathVariable Long commentId, @RequestParam(name = "userId") Long userId) {
        commentHeartService.heartComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}/remove-emotion")
    public ResponseEntity<Void> removeCommentEmotion(@PathVariable Long commentId, @RequestParam(name = "userId") Long userId) {
        commentEmotionService.removeEmotion(commentId, userId);
        return ResponseEntity.ok().build();
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

        // Chuyển đổi các entity liên quan sang DTO tương ứng nếu cần
        return commentDTO;
    }

}
