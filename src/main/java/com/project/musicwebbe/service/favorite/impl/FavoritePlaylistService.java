package com.project.musicwebbe.service.favorite.impl;

import com.project.musicwebbe.entities.FavoritePlaylist;
import com.project.musicwebbe.repository.FavoritePlaylistRepository;
import com.project.musicwebbe.service.favorite.IFavoritePlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePlaylistService implements IFavoritePlaylistService {
    @Autowired
    private FavoritePlaylistRepository favoritePlaylistRepository;

    @Override
    public List<FavoritePlaylist> findAll() {
        return favoritePlaylistRepository.findAll();
    }

    @Override
    public Page<FavoritePlaylist> findAll(Pageable pageable) {
        return favoritePlaylistRepository.findAll(pageable);
    }

    @Override
    public FavoritePlaylist findById(Long id) {
        return favoritePlaylistRepository.findById(id).orElse(null);
    }

    @Override
    public void save(FavoritePlaylist favoritePlaylist) {
        favoritePlaylistRepository.save(favoritePlaylist);
    }

    @Override
    public void remove(Long id) {
        favoritePlaylistRepository.deleteById(id);
    }

    @Override
    public void deleteByPlaylistIdAndUserCode(Long playlistId, String userCode) {
        favoritePlaylistRepository.deleteByPlaylistPlaylistIdAndAppUserUserCodeContaining(playlistId, userCode);
    }
}
