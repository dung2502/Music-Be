package com.project.musicwebbe.service.favorite;

import com.project.musicwebbe.entities.FavoritePlaylist;
import com.project.musicwebbe.service.IGeneralService;

public interface IFavoritePlaylistService extends IGeneralService<FavoritePlaylist> {
    void deleteByPlaylistIdAndUserCode(Long playlistId, String userCode);
}
