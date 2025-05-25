package com.project.musicwebbe.dto.commentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOfCommentDTO {

    private Long userId;

    private String email;

    private String fullName;

    private String userCode;

    private String avatar;
}
