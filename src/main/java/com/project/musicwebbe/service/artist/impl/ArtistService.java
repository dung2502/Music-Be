package com.project.musicwebbe.service.artist.impl;

import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.repository.ArtistRepository;
import com.project.musicwebbe.service.artist.IArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService implements IArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public Page<Artist> findAll(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    @Override
    public List<Artist> searchAllByArtistName(String artistName) {
        return artistRepository.searchAllByArtistNameContaining(artistName);
    }

    @Override
    public Page<Artist> searchAllByArtistName(String artistName, Pageable pageable) {
        return artistRepository.searchAllByArtistNameContainingAndArtistStatusIsFalse(artistName, pageable);
    }

    @Override
    public Page<Artist> searchAllFavoriteArtistsByUserId(Long userId, Pageable pageable) {
        return artistRepository.findAllFavoriteArtistsByUserId(userId, pageable);
    }

    @Override
    public Artist findById(Long id) {
        return artistRepository.findById(id).orElse(null);
    }

    @Override
    public Artist findByName(String artistName) {
        return artistRepository.findByArtistNameContaining(artistName);
    }

    @Override
    public void save(Artist artist) {
        artistRepository.save(artist);
    }

    @Override
    public void remove(Long id) {
        artistRepository.deleteById(id);
    }

    @Override
    public Artist saveD(Artist artist) {
        return artistRepository.save(artist);
    }
}
