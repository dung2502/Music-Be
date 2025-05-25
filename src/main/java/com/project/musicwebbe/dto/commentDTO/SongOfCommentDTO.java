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
public class SongOfCommentDTO {
    private Long songId;

    private String title;

    private LocalDateTime dateCreate;

    private String lyrics;

    private String songUrl;

    private int duration;

    private String coverImageUrl;
}
