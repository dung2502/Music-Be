package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.dto.artistDTO.AlbumOfArtistDTO;
import com.project.musicwebbe.dto.artistDTO.ArtistDTO;
import com.project.musicwebbe.dto.artistDTO.SongOfArtistDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.entities.Genre;
import com.project.musicwebbe.service.artist.impl.ArtistService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/artists")
public class ArtistRestController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long artistId) {
        Artist artist = artistService.findById(artistId);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        ArtistDTO artistDTO = convertEntityToDTO.convertToArtistDTO(artist);
        return ResponseEntity.ok(artistDTO);
    }

    @GetMapping("/name/{artistName}")
    public ResponseEntity<ArtistDTO> getArtistByName(@PathVariable String artistName) {
        Artist artist = artistService.findByName(artistName);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        ArtistDTO artistDTO = convertEntityToDTO.convertToArtistDTO(artist);
        return ResponseEntity.ok(artistDTO);
    }


}
