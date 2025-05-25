package com.project.musicwebbe.service.genre.impl;

import com.project.musicwebbe.entities.Genre;
import com.project.musicwebbe.repository.GenreRepository;
import com.project.musicwebbe.service.genre.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService implements IGenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Page<Genre> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    @Override
    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public Genre findById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(Long id) {
        genreRepository.deleteById(id);
    }
}
