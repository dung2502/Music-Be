package com.project.musicwebbe.service.song;

import com.project.musicwebbe.entities.UserListen;
import com.project.musicwebbe.service.IGeneralService;

import java.time.LocalDate;
import java.util.List;

public interface IUserListenService  extends IGeneralService<UserListen> {

    List<UserListen> findTop10RecentByUserId(Long userId);

    UserListen findBySongIdAndUserIdAndDate(Long songId, Long userId, LocalDate listenedAt);
}
