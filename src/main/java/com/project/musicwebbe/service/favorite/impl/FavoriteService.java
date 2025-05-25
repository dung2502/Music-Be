package com.project.musicwebbe.service.favorite.impl;

import com.project.musicwebbe.entities.Favorite;
import com.project.musicwebbe.repository.FavoriteRepository;
import com.project.musicwebbe.service.favorite.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService implements IFavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> findAll() {
        return favoriteRepository.findAll();
    }

    @Override
    public Page<Favorite> findAll(Pageable pageable) {
        return favoriteRepository.findAll(pageable);
    }

    @Override
    public Favorite findById(Long id) {
        return favoriteRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    @Override
    public void remove(Long id) {
        favoriteRepository.deleteById(id);
    }
    @Override
    public List<Favorite> findAllByAppUser_UserId(Long userId) {
        return favoriteRepository.findAllByAppUser_UserId(userId);
    }

    @Override
    public List<Favorite> findAllByUserIdAndSearch(Long userId, String search) {
        return favoriteRepository.findAllByUserIdAndSearch(userId, search);
    }

    @Override
    public void deleteBySongIdAndUserCode(Long songId, String userCode) {
        favoriteRepository.deleteBySongSongIdAndAppUserUserCodeContaining(songId, userCode);
    }
}
