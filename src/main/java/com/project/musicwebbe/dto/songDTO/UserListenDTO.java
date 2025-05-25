package com.project.musicwebbe.dto.songDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListenDTO {
    private Long listensId;

    private LocalDate listenedAt;


    private SimpleUserDTO user;

    private SongDTO song;
}
