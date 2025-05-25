package com.project.musicwebbe.service.favorite;

import com.project.musicwebbe.entities.FavoriteAlbum;
import com.project.musicwebbe.service.IGeneralService;

public interface IFavoriteAlbumService extends IGeneralService<FavoriteAlbum> {
    void deleteByAlbumIdAndUserCode(Long albumId, String userCode);

}
