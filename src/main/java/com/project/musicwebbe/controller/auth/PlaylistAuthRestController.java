package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.controller.component.PlaylistCodeGenerator;
import com.project.musicwebbe.dto.playlistDTO.PlaylistDTO;
import com.project.musicwebbe.entities.PlayListSong;
import com.project.musicwebbe.entities.Playlist;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IUserService;
import com.project.musicwebbe.service.permission.impl.UserService;
import com.project.musicwebbe.service.playlist.impl.PlaylistService;
import com.project.musicwebbe.service.song.impl.SongListenService;
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

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/playlists")
public class PlaylistAuthRestController {

    @Autowired
    private IUserService userService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SongService songService;
    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;
    @Autowired
    private GetCurrentUser getCurrentUser;
    @Autowired
    private SongListenService songListenService;
    @Autowired
    private PlaylistCodeGenerator codeGenerator;


    @GetMapping("/findAll")
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.findAll();
        if (playlists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<PlaylistDTO> playlistDTOS = playlists.stream()
                .map(convertEntityToDTO::convertToPlaylistDTO)
                .toList();
        return ResponseEntity.ok(playlistDTOS);
    }

    @GetMapping("/{playlistId:\\d+}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long playlistId) {
        Playlist playlist = playlistService.findById(playlistId);
        if (playlist == null) {
            return ResponseEntity.notFound().build();
        }
        PlaylistDTO playlistDTO = convertEntityToDTO.convertToPlaylistDTO(playlist);
        return ResponseEntity.ok(playlistDTO);
    }

    @GetMapping("/{playlistId}/total-listens")
    public ResponseEntity<Integer> getTotalListensByPlaylist(@PathVariable Long playlistId) {
        Playlist playlist = playlistService.findById(playlistId);
        if (playlist == null) {
            return ResponseEntity.notFound().build();
        }

        List<PlayListSong> songs = playlist.getPlayListSongs();
        int total = 0;

        for (PlayListSong songEntry : songs) {
            Long songId = songEntry.getSong().getSongId();
            Integer listens = songListenService.getTotalListenBySongId(songId);
            total += listens != null ? listens : 0;
        }

        return ResponseEntity.ok(total);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistDTO>> getPlaylistByUserId(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.searchAllPlaylistsByUserId(userId);
        if (playlists == null) {
            return ResponseEntity.notFound().build();
        }
        List<PlaylistDTO> playlistDTOS = playlists.stream()
                .map(convertEntityToDTO::convertToPlaylistDTO)
                .toList();
        return ResponseEntity.ok(playlistDTOS);
    }

    @GetMapping("/getAllWithPage")
    public ResponseEntity<Page<PlaylistDTO>> getAllPageAndSearchPlaylists(
            @RequestParam(value = "playlistName", defaultValue = "") String playlistName,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        Page<Playlist> playlists = playlistService.searchAllByPlaylistName(playlistName, PageRequest.of(page, 5));
        Page<PlaylistDTO> playlistDTOS = playlists.map(convertEntityToDTO::convertToPlaylistDTO);
        return ResponseEntity.ok(playlistDTOS);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<PlaylistDTO>> getFavoritePlaylists(
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
        Page<Playlist> playlists = playlistService.searchAllFavoritePlaylistsByUserId(user.getUserId(), pageRequest);
        if (playlists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<PlaylistDTO> playlistDTOS = playlists.map(convertEntityToDTO::convertToPlaylistDTO);
        return ResponseEntity.ok(playlistDTOS);
    }

    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist, @RequestParam Long userId) {
        AppUser currentUser = userService.findById(userId);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("User không tồn tại");
        }

        playlist.setAppUser(currentUser);
        playlist.setDateCreate(LocalDateTime.now());
        playlist.setPlaylistCode(codeGenerator.generate());

        if (playlist.getPlayListSongs() != null) {
            for (PlayListSong playListSong : playlist.getPlayListSongs()) {
                playListSong.setPlaylist(playlist);
            }
        }

        playlistService.save(playlist);
        return ResponseEntity.ok(200);
    }


    @PutMapping("/update/{playlistId}")
    public ResponseEntity<?> updatePlaylist(@PathVariable Long playlistId, @Validated @RequestBody Playlist playlist) {
        Playlist existingPlaylist = playlistService.findById(playlistId);
        if (existingPlaylist == null) {
            return ResponseEntity.notFound().build();
        }

        existingPlaylist.setPlaylistName(playlist.getPlaylistName());
        existingPlaylist.setDescription(playlist.getDescription());
        existingPlaylist.setCoverImageUrl(playlist.getCoverImageUrl());

        List<PlayListSong> newPlayListSongs = playlist.getPlayListSongs() != null ? playlist.getPlayListSongs() : new ArrayList<>();
        List<PlayListSong> currentPlayListSongs = existingPlaylist.getPlayListSongs();
        currentPlayListSongs.removeIf(old -> newPlayListSongs.stream()
                .noneMatch(newSong -> newSong.getSong() != null &&
                        newSong.getSong().getSongId().equals(old.getSong().getSongId())));

        Set<Long> currentSongIds = currentPlayListSongs.stream()
                .map(s -> s.getSong().getSongId())
                .collect(Collectors.toSet());

        for (PlayListSong newSong : newPlayListSongs) {
            if (newSong == null || newSong.getSong() == null || newSong.getSong().getSongId() == null) {
                return ResponseEntity.badRequest().body("Một hoặc nhiều bài hát không hợp lệ.");
            }

            if (!currentSongIds.contains(newSong.getSong().getSongId())) {
                Song songEntity = songService.findById(newSong.getSong().getSongId());
                if (songEntity == null) {
                    return ResponseEntity.badRequest().body("Bài hát với ID " + newSong.getSong().getSongId() + " không tồn tại.");
                }

                newSong.setSong(songEntity);
                newSong.setPlaylist(existingPlaylist);
                newSong.setDateAdd(LocalDateTime.now());
                currentPlayListSongs.add(newSong);
            }
        }

        existingPlaylist.setPlayListSongs(currentPlayListSongs);
        playlistService.save(existingPlaylist);
        return ResponseEntity.ok(200);
    }

    @PostMapping("/{playlistId}/add-song/{songId}")
    public ResponseEntity<?> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistService.findById(playlistId);
        if (playlist == null) {
            return ResponseEntity.badRequest().body("Playlist không tồn tại");
        }

        Song song = songService.findById(songId);
        if (song == null) {
            return ResponseEntity.badRequest().body("Bài hát không tồn tại");
        }

        boolean songExists = playlist.getPlayListSongs().stream()
                .anyMatch(playlistSong -> playlistSong.getSong().getSongId().equals(songId));
        if (songExists) {
            return ResponseEntity.badRequest().body("Bài hát đã tồn tại trong playlist");
        }

        PlayListSong playListSong = PlayListSong.builder()
                .playlist(playlist)
                .song(song)
                .build();

        playlist.getPlayListSongs().add(playListSong);
        playlistService.save(playlist);

        return ResponseEntity.ok("Đã thêm bài hát vào playlist");
    }



    @PutMapping("remove/{playlistId}")
    public ResponseEntity<?> removePlaylist(@PathVariable Long playlistId) {
        try {
            Playlist existingPlaylist = playlistService.findById(playlistId);
            if (existingPlaylist == null) {
                return ResponseEntity.notFound().build();
            }
            playlistService.deletePlaylistSongs(playlistId);
            playlistService.deletePlaylist(playlistId);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while removing playlist", e);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-playlist-user")
    public ResponseEntity<?> createPlaylistUser(@RequestBody Playlist playlist, @RequestParam Long userId) {
        AppUser currentUser = userService.findById(userId);

        if (currentUser == null) {
            return ResponseEntity.badRequest().body("User không tồn tại");
        }
        if (playlist.getCoverImageUrl() == null || playlist.getCoverImageUrl().isBlank()) {
            playlist.setCoverImageUrl("https://firebasestorage.googleapis.com/v0/b/music-g-649ea.appspot.com/o/multipleImages%2Fmymussic.png?alt=media&token=184b337f-6f4b-48db-9a8f-416911c628c7"); // Thay bằng link thật của bạn
        }
        playlist.setAppUser(currentUser);
        playlist.setDateCreate(LocalDateTime.now());

        String playlistCodeDefault = "MUSIC-";
        String randomLetters = generateRandomString();
        playlist.setPlaylistCode(playlistCodeDefault + randomLetters);

        playlistService.save(playlist);

        return ResponseEntity.ok(200);
    }

    private String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }

        return result.toString();
    }

}
