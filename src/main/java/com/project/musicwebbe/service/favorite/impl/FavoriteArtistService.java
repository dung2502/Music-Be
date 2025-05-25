package com.project.musicwebbe.service.favorite.impl;

import com.project.musicwebbe.entities.FavoriteArtist;
import com.project.musicwebbe.repository.FavoriteArtistRepository;
import com.project.musicwebbe.service.favorite.IFavoriteArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteArtistService implements IFavoriteArtistService {
    @Autowired
    private FavoriteArtistRepository favoriteArtistRepository;

    @Override
    public List<FavoriteArtist> findAll() {
        return favoriteArtistRepository.findAll();
    }

    @Override
    public Page<FavoriteArtist> findAll(Pageable pageable) {
        return favoriteArtistRepository.findAll(pageable);
    }

    @Override
    public FavoriteArtist findById(Long id) {
        return favoriteArtistRepository.findById(id).orElse(null);
    }

    @Override
    public void save(FavoriteArtist favoriteArtist) {
        favoriteArtistRepository.save(favoriteArtist);
    }

    @Override
    public void remove(Long id) {
        favoriteArtistRepository.deleteById(id);
    }

    @Override
    public void deleteByArtistIdAndUserCode(Long artistId, String userCode) {
        favoriteArtistRepository.deleteByArtistArtistIdAndAppUserUserCodeContaining(artistId, userCode);
    }
}
