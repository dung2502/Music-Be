package com.project.musicwebbe.dto.playlistDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOfPlaylistDTO {

    private Long userId;

    private String fullName;
}
