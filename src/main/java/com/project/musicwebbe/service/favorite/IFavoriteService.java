package com.project.musicwebbe.service.favorite;

import com.project.musicwebbe.entities.Favorite;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFavoriteService extends IGeneralService<Favorite> {
    List<Favorite> findAllByAppUser_UserId(Long userId);
    List<Favorite> findAllByUserIdAndSearch( Long userId,  String search);
    void deleteBySongIdAndUserCode(Long songId, String userCode);
}
