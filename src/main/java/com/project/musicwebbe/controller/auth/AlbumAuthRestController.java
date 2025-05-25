package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.controller.notification.NotificationWebSocketHandler;
import com.project.musicwebbe.dto.albumDTO.AlbumDTO;
import com.project.musicwebbe.dto.songDTO.SongDTO;
import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.entities.Notification;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.album.impl.AlbumService;
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
@RequestMapping("/api/auth/albums")
public class AlbumAuthRestController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @Autowired
    private GetCurrentUser getCurrentUser;

    @GetMapping
    public ResponseEntity<Page<AlbumDTO>> getAllAlbums(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "artistName", defaultValue = "") String artistName,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        if (page<0) {
            page = 0;
        }
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date_create"));
        Page<Album> albums = albumService.searchAllByTitleAndArtistNameAndAlbumStatusIsFalse(title, artistName, pageRequest);
        if (albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Chuyển đổi từ Album sang AlbumDTO
        Page<AlbumDTO> albumDTOs = albums.map(convertEntityToDTO::convertToAlbumDTO);
        return ResponseEntity.ok(albumDTOs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AlbumDTO>> getAllAlbums(){
        List<Album> albums = albumService.findAll();
        if (albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<AlbumDTO> albumDTOs = albums.stream()
                .map(convertEntityToDTO::convertToAlbumDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(albumDTOs);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<AlbumDTO>> getFavoriteAlbums(
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
        Page<Album> albums = albumService.searchAllFavoriteAlbumsByUserId(user.getUserId(), pageRequest);
        if (albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<AlbumDTO> albumDTOS = albums.map(convertEntityToDTO::convertToAlbumDTO);
        return ResponseEntity.ok(albumDTOS);
    }

    @PostMapping
    public ResponseEntity<?> createAlbum(@Validated @RequestBody Album album) {
        album.setDateCreate(LocalDateTime.now());
        Album album1 = albumService.saveD(album);
        return ResponseEntity.ok(album1);
    }

    @PutMapping("/{albumId}")
    public ResponseEntity<?> updateAlbum(@PathVariable Long albumId, @Validated @RequestBody Album album) {
        var albumEntity = albumService.findById(albumId);
        if (albumEntity == null) {
            return ResponseEntity.notFound().build();
        }
        albumService.save(album);
        return ResponseEntity.ok(200);
    }

        @PutMapping("remove/{albumId}")
        public ResponseEntity<?> removeAlbum(@PathVariable Long albumId) {
            try {
                Album albumEntity = albumService.findById(albumId);
                if (albumEntity == null) {
                    return ResponseEntity.notFound().build();
            }
                albumEntity.setAlbumStatus(true);
                albumService.save(albumEntity);

            }
            catch (Exception e) {
                throw new RuntimeException("Error occurred while removing album", e);
            }
            return ResponseEntity.noContent().build();
        }
    }
