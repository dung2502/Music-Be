package com.project.musicwebbe.dto.commentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;

    private String content;

    private UserOfCommentDTO user;

    private SongOfCommentDTO song;

    private Page<CommentDTO> replies;

    private List<CommentLikeDTO> likes;

    private List<CommentDislikeDTO> dislikes;

    private List<CommentHahaDTO> hahas;

    private List<CommentWowDTO> wows;

    private List<CommentHeartDTO> hearts;

    private LocalDateTime createdAt;
}
