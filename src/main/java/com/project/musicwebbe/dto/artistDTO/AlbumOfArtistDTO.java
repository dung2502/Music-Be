package com.project.musicwebbe.dto.artistDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumOfArtistDTO {

    private Long albumId;

    private String title;

    private LocalDateTime dateCreate;

    private String provide;

    private String coverImageUrl;
}
