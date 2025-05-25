package com.project.musicwebbe.service.song;

import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISongService extends IGeneralService<Song> {
    Song saveD(Song song);

    List<Song> searchAllByTitle(String title);

    Page<Song> findAllTopSongByNational(String national, Pageable pageable);

    Page<Song> searchAllByTitleAndArtistName(String title, String artistName,Pageable pageable);

    Page<Song> searchAllFavoriteSongsByUserId(Long userId, Pageable pageable);

    List<Song> findNewSongRatings();

    List<Song> findNewSongsWithNational(String national);

    List<Song> findTop100Songs();

    List<Song> findTopThreeSongsInSevenDays();
}
