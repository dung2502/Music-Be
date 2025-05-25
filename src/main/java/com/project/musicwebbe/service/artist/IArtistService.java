package com.project.musicwebbe.service.artist;

import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArtistService extends IGeneralService<Artist> {
    Artist saveD(Artist artist);

    List<Artist> searchAllByArtistName(String artistName);

    Page<Artist> searchAllByArtistName(String artistName, Pageable pageable);

    Artist findByName(String artistName);

    Page<Artist> searchAllFavoriteArtistsByUserId(Long userId, Pageable pageable);

}
