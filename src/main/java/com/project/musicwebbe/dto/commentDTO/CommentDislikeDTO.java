package com.project.musicwebbe.dto.commentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDislikeDTO {
    private Long dislikeId;

    private UserOfCommentDTO user;

    private LocalDateTime dislikedAt;
}
