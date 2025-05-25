package com.project.musicwebbe.service.song.impl;

import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.SongListen;
import com.project.musicwebbe.repository.SongListenRepository;
import com.project.musicwebbe.repository.SongRepository;
import com.project.musicwebbe.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class SongService implements ISongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongListenRepository songListenRepository;

    @Override
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public Page<Song> searchAllByTitleAndArtistName(String title, String artistName,Pageable pageable) {
        return songRepository.findAllByTitleAndArtist(title, artistName, pageable);
    }

    @Override
    public Page<Song> findAll(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    @Override
    public List<Song> searchAllByTitle(String title) {
        return songRepository.searchAllByTitleContaining(title);
    }

    @Override
    public List<Song> findTopThreeSongsInSevenDays() {
        List<Song> songs = songRepository.findTopThreeSongsInSevenDays(LocalDate.now().minusDays(7));
        for (Song song : songs) {
            List<SongListen> songListens = songListenRepository.findAllBySongIdInSevenDays(song.getSongId());
            song.setSongListens(songListens);
        }
        return songs;
    }


    @Override
    public Page<Song> findAllTopSongByNational(String national, Pageable pageable) {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return songRepository.findAllTopSongsByNational(national, startOfWeek, pageable);
    }

    @Override
    public Page<Song> searchAllFavoriteSongsByUserId(Long userId, Pageable pageable) {
        return songRepository.findAllFavoriteSongsByUserId(userId, pageable);
    }

    @Override
    public List<Song> findNewSongRatings() {
        return songRepository.findNewSongRatings();
    }

    @Override
    public List<Song> findNewSongsWithNational(String national) {
        return songRepository.findNewSongsWithNational(national);
    }

    @Override
    public List<Song> findTop100Songs() {
        return songRepository.findTop100Songs();
    }

    @Override
    public Song findById(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Song song) {
        songRepository.save(song);
    }

    @Override
    public void remove(Long id) {
        songRepository.deleteById(id);
    }
    @Override
    public Song saveD(Song song) {
        return songRepository.save(song);
    }
}
