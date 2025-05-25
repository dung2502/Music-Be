package com.project.musicwebbe.service.song.impl;

import com.project.musicwebbe.entities.SongListen;
import com.project.musicwebbe.repository.SongListenRepository;
import com.project.musicwebbe.service.song.ISongListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SongListenService implements ISongListenService {

    @Autowired
    private SongListenRepository songListenRepository;

    @Override
    public List<SongListen> findAll() {
        return songListenRepository.findAll();
    }

    @Override
    public Page<SongListen> findAll(Pageable pageable) {
        return songListenRepository.findAll(pageable);
    }

    @Override
    public SongListen findById(Long id) {
        return songListenRepository.findById(id).orElse(null);
    }

    @Override
    public void save(SongListen songListen) {
        songListenRepository.save(songListen);
    }

    @Override
    public void remove(Long id) {
        songListenRepository.deleteById(id);
    }

    @Override
    public SongListen findBySongIdToday(Long songId) {
        return songListenRepository.findBySongSongIdAndDateCreate(songId, LocalDate.now());
    }
}
