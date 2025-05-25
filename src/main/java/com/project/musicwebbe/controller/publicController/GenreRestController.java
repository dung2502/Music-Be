package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.entities.Genre;
import com.project.musicwebbe.service.genre.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/genres")
public class GenreRestController {

    @Autowired
    private IGenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        System.out.println("call");
        List<Genre> genres = genreService.findAll();
        if (genres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        genreService.save(genre);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }
}