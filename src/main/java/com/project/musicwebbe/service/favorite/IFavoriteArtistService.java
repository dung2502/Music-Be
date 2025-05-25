package com.project.musicwebbe.service.favorite;

import com.project.musicwebbe.entities.FavoriteArtist;
import com.project.musicwebbe.service.IGeneralService;

public interface IFavoriteArtistService extends IGeneralService<FavoriteArtist> {
    void deleteByArtistIdAndUserCode(Long artistId, String userCode);

}
