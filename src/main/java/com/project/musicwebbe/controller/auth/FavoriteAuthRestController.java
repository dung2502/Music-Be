package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.favoriteDTO.FavoriteDTOT;
import com.project.musicwebbe.entities.*;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.favorite.IFavoriteAlbumService;
import com.project.musicwebbe.service.favorite.IFavoriteArtistService;
import com.project.musicwebbe.service.favorite.IFavoritePlaylistService;
import com.project.musicwebbe.service.favorite.IFavoriteService;
import com.project.musicwebbe.service.permission.impl.UserService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import com.project.musicwebbe.util.GetCurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth/favorites")
public class FavoriteAuthRestController {

    @Autowired
    private IFavoriteService favoriteService;

    @Autowired
    private IFavoriteAlbumService favoriteAlbumService;

    @Autowired
    private IFavoritePlaylistService favoritePlaylistService;

    @Autowired
    private IFavoriteArtistService favoriteArtistService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @Autowired
    private GetCurrentUser getCurrentUser;

    @GetMapping
    public ResponseEntity<List<Favorite>> getFavorites() {
        List<Favorite> favorites = favoriteService.findAll();
        if (favorites.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<FavoriteDTOT>> getAllFavorites(Long userId) {
        List<Favorite> favorites = favoriteService.findAllByAppUser_UserId(userId);
        if (favorites.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<FavoriteDTOT> favoriteDTOTS = favorites.stream()
                .map(convertEntityToDTO::convertToFavoriteDTO)
                .toList();
        return ResponseEntity.ok(favoriteDTOTS);
    }

    @PostMapping("/song")
    public ResponseEntity<?> addFavoriteSong(@Validated @RequestBody Song song) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Favorite favorite = new Favorite();
        favorite.setAppUser(user);
        favorite.setAddedAt(LocalDateTime.now());
        favorite.setSong(song);
        favoriteService.save(favorite);
        return ResponseEntity.ok(200);
    }

    @PostMapping("/album")
    public ResponseEntity<?> addFavoriteAlbum(@Validated @RequestBody Album album) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        FavoriteAlbum favoriteAlbum = new FavoriteAlbum();
        favoriteAlbum.setAppUser(user);
        favoriteAlbum.setAddedAt(LocalDateTime.now());
        favoriteAlbum.setAlbum(album);
        favoriteAlbumService.save(favoriteAlbum);
        return ResponseEntity.ok(200);
    }

    @PostMapping("/playlist")
    public ResponseEntity<?> addFavoritePlaylist(@Validated @RequestBody Playlist playlist) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        FavoritePlaylist favoritePlaylist = new FavoritePlaylist();
        favoritePlaylist.setAppUser(user);
        favoritePlaylist.setAddedAt(LocalDateTime.now());
        favoritePlaylist.setPlaylist(playlist);
        favoritePlaylistService.save(favoritePlaylist);
        return ResponseEntity.ok(200);
    }

    @PostMapping("/artist")
    public ResponseEntity<?> addFavoriteArtist(@Validated @RequestBody Artist artist) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        FavoriteArtist favoriteArtist = new FavoriteArtist();
        favoriteArtist.setAppUser(user);
        favoriteArtist.setAddedAt(LocalDateTime.now());
        favoriteArtist.setArtist(artist);
        favoriteArtistService.save(favoriteArtist);
        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<?> deleteFavorite(@PathVariable(name = "favoriteId") Long favoriteId){
        favoriteService.remove(favoriteId);
        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/song")
    public ResponseEntity<?> deleteFavoriteSong(@RequestBody Song song) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        favoriteService.deleteBySongIdAndUserCode(song.getSongId(), user.getUserCode());
        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/album")
    public ResponseEntity<?> deleteFavoriteAlbum(@RequestBody Album album) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        favoriteAlbumService.deleteByAlbumIdAndUserCode(album.getAlbumId(), user.getUserCode());
        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/artist")
    public ResponseEntity<?> deleteFavoriteArtist(@RequestBody Artist artist) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        favoriteArtistService.deleteByArtistIdAndUserCode(artist.getArtistId(), user.getUserCode());
        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/playlist")
    public ResponseEntity<?> deleteFavoritePlaylist(@RequestBody Playlist playlist) {
        AppUser user = getCurrentUser.getUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        favoritePlaylistService.deleteByPlaylistIdAndUserCode(playlist.getPlaylistId(), user.getUserCode());
        return ResponseEntity.ok(200);
    }
}
