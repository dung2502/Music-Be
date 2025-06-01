package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.albumDTO.AlbumDTO;
import com.project.musicwebbe.dto.artistDTO.ArtistDTO;
import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.entities.Genre;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.artist.impl.ArtistService;
import com.project.musicwebbe.service.permission.impl.UserService;
import com.project.musicwebbe.service.song.ISongService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth/artists")
public class ArtistAuthRestController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @Autowired
    private GetCurrentUser getCurrentUser;

    @GetMapping
    public ResponseEntity<Page<ArtistDTO>> getAllPageArtists(
            @RequestParam(name = "artistName", defaultValue = "") String artistName,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        if (page < 0) {
            page = 0;
        }
        Page<Artist> artists = artistService.searchAllByArtistName(artistName, PageRequest.of(page, 10));
        if (artists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<ArtistDTO> artistDTOS = artists.map(convertEntityToDTO::convertToArtistDTO);
        return ResponseEntity.ok(artistDTOS);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<Artist> artists = artistService.findAll();
        if (artists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ArtistDTO> artistDTOS = artists.stream()
                .map(convertEntityToDTO::convertToArtistDTO)
                .toList();
        return ResponseEntity.ok(artistDTOS);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Page<ArtistDTO>> getFavoriteArtists(
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
        Page<Artist> artists = artistService.searchAllFavoriteArtistsByUserId(user.getUserId(), pageRequest);
        if (artists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<ArtistDTO> artistDTOS = artists.map(convertEntityToDTO::convertToArtistDTO);
        return ResponseEntity.ok(artistDTOS);
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@Validated @RequestBody Artist artist) {
        Artist savedArtist = artistService.saveD(artist);

        return ResponseEntity.ok(savedArtist);
    }


    @PutMapping("/{artistId}")
    public ResponseEntity<?> updateArtist(@PathVariable Long artistId, @Validated @RequestBody Artist artist) {
        var artistEntity = artistService.findById(artistId);
        if (artistEntity == null) {
            return ResponseEntity.notFound().build();
        }
        artistService.save(artist);
        return ResponseEntity.ok(200);
    }

    @PutMapping("remove/{artistId}")
    public ResponseEntity<?> removeArtist(@PathVariable Long artistId) {
        try {
            Artist artistEntity = artistService.findById(artistId);
            if (artistEntity == null) {
                return ResponseEntity.notFound().build();
            }
            artistEntity.setArtistStatus(true);
            artistService.save(artistEntity);

        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred while removing album", e);
        }
        return ResponseEntity.noContent().build();
    }
}