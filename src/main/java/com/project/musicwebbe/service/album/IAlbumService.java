package com.project.musicwebbe.service.album;

import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlbumService extends IGeneralService<Album> {

    Album saveD(Album album);

    List<Album> findByAlbumName(String albumName);

    Page<Album> searchAllByTitleAndArtistNameAndAlbumStatusIsFalse(String title, String artistName, Pageable pageable);

    Page<Album> searchAllFavoriteAlbumsByUserId(Long userId, Pageable pageable);

    List<Album> findNewAlbumsWithNational(String national);

    Page<Album> getTopSixAlbumsBestListen(Pageable pageable);

}
