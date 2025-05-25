package com.project.musicwebbe.service.favorite.impl;

import com.project.musicwebbe.entities.FavoriteAlbum;
import com.project.musicwebbe.repository.FavoriteAlbumRepository;
import com.project.musicwebbe.service.favorite.IFavoriteAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteAlbumService implements IFavoriteAlbumService {
    @Autowired
    private FavoriteAlbumRepository favoriteAlbumRepository;

    @Override
    public List<FavoriteAlbum> findAll() {
        return favoriteAlbumRepository.findAll();
    }

    @Override
    public Page<FavoriteAlbum> findAll(Pageable pageable) {
        return favoriteAlbumRepository.findAll(pageable);
    }

    @Override
    public FavoriteAlbum findById(Long id) {
        return favoriteAlbumRepository.findById(id).orElse(null);
    }

    @Override
    public void save(FavoriteAlbum favoriteAlbum) {
        favoriteAlbumRepository.save(favoriteAlbum);
    }

    @Override
    public void remove(Long id) {
        favoriteAlbumRepository.deleteById(id);
    }

    @Override
    public void deleteByAlbumIdAndUserCode(Long albumId, String userCode) {
        favoriteAlbumRepository.deleteByAlbumAlbumIdAndAppUserUserCodeContaining(albumId, userCode);
    }
}
