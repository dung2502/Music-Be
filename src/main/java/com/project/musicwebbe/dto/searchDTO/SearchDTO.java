package com.project.musicwebbe.dto.searchDTO;

import com.project.musicwebbe.dto.songDTO.AlbumOfSongDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {
    private Long searchId;

    private String searchTitle;

    private String coverImageUrl;

    private List<ArtistOfSearch> artists;

    private String type;
}
