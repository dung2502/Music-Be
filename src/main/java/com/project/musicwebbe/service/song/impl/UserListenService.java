package com.project.musicwebbe.service.song.impl;


import com.project.musicwebbe.entities.UserListen;
import com.project.musicwebbe.repository.UserListenRepository;
import com.project.musicwebbe.service.song.IUserListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserListenService implements IUserListenService {

    @Autowired
    private UserListenRepository userListenRepository;

    @Override
    public List<UserListen> findAll() {
        return userListenRepository.findAll();
    }

    @Override
    public Page<UserListen> findAll(Pageable pageable) {
        return userListenRepository.findAll(pageable);
    }

    @Override
    public UserListen findById(Long id) {
        return userListenRepository.findById(id).orElse(null);
    }

    @Override
    public void save(UserListen userListen) {
        userListenRepository.save(userListen);
    }

    @Override
    public void remove(Long id) {
        userListenRepository.deleteById(id);
    }

    @Override
    public List<UserListen> findTop10RecentByUserId(Long userId) {
        return userListenRepository.findTop10RecentByUserId(userId);
    }

    @Override
    public UserListen findBySongIdAndUserIdAndDate(Long songId, Long userId, LocalDate listenedAt) {
        return userListenRepository.findBySongSongIdAndAppUserUserIdAndListenedAt(songId, userId, listenedAt);
    }
}
