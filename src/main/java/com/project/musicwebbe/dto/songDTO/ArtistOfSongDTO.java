package com.project.musicwebbe.dto.songDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistOfSongDTO {
    private Long artistId;

    private String artistName;

    private String avatar;
}
