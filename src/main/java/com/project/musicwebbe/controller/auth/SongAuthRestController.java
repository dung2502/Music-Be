package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.songDTO.SongDTO;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.impl.UserService;
import com.project.musicwebbe.service.song.impl.SongService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import com.project.musicwebbe.util.GetCurrentUser;
import com.project.musicwebbe.util.SortList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/songs")
public class SongAuthRestController {
    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @Autowired
    private GetCurrentUser getCurrentUser;

    @GetMapping
    public ResponseEntity<Page<SongDTO>> getAllPageSongs(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "artistName", defaultValue = "") String artistName,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        if (page < 0) {
            page = 0;
        }
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date_create"));
        Page<Song> songs = songService.searchAllByTitleAndArtistName(title, artistName, pageRequest);
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<SongDTO> songDTOS = songs.map(convertEntityToDTO::convertToSongDTO);
        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<Song> songs = songService.findAll();
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<SongDTO> songDTOS = songs.stream()
                .map(convertEntityToDTO::convertToSongDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<SongDTO>> getFavoriteSongs(
            @RequestParam(name = "sort", defaultValue = "") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "100") int size
    ) {
        if (page < 0) {
            page = 0;
        }
        if (size < 0) {
            size = 100;
        }
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<Sort.Order> orders = new ArrayList<>();
        switch (sort) {
            case "title":
                orders.add(SortList.createSortOrder("title", direction));
                break;
            default:
                orders.add(SortList.createSortOrder("f.addedAt", direction));
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(orders));
        Page<Song> songs = songService.searchAllFavoriteSongsByUserId(user.getUserId(), pageRequest);
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<SongDTO> songDTOS = songs.map(convertEntityToDTO::convertToSongDTO);
        return ResponseEntity.ok(songDTOS);
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@Validated @RequestBody Song song) {
        System.out.println(song);
        song.setDateCreate(LocalDateTime.now());
        Song song1 = songService.saveD(song);
        return ResponseEntity.ok(song1);
    }

    @PutMapping("/{songId}")
    public ResponseEntity<Song> updateSong(@PathVariable Long songId, @RequestBody Song song) {
        var songEntity = songService.findById(songId);
        if (songEntity == null) {
            return ResponseEntity.notFound().build();
        }
        songService.save(song);
        return ResponseEntity.ok(song);
    }



}
