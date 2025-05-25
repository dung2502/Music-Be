package com.project.musicwebbe.dto.albumDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistOfAlbumDTO {
    private Long artistId;

    private String artistName;

    private String avatar;
}
