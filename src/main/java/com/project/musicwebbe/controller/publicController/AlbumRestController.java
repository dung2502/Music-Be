package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.dto.albumDTO.ArtistOfAlbumDTO;
import com.project.musicwebbe.dto.albumDTO.AlbumDTO;
import com.project.musicwebbe.dto.albumDTO.SongOfAlbumDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.service.album.impl.AlbumService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/public/albums")
public class AlbumRestController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @GetMapping("/suggestedAlbums")
    public ResponseEntity<Page<AlbumDTO>> getAllSuggestedAlbums() {
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "dateCreate"));

        Page<Album> albums = albumService.findAll(pageRequest);
        Page<AlbumDTO> albumDTOs = albums.map(convertEntityToDTO::convertToAlbumDTO);
        return ResponseEntity.ok(albumDTOs);
    }

    @GetMapping("/new-albums-release")
    public ResponseEntity<List<AlbumDTO>> getAllNewAlbumsRelease(@RequestParam(name = "national", defaultValue = "") String national) {
        List<Album> albums = albumService.findNewAlbumsWithNational(national);
        if (albums.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<AlbumDTO> albumDTOS = albums.stream().map(convertEntityToDTO::convertToAlbumDTO).toList();
        return ResponseEntity.ok(albumDTOS);
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long albumId) {
        Album album = albumService.findById(albumId);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        AlbumDTO albumDTO = convertEntityToDTO.convertToAlbumDTO(album);
        return ResponseEntity.ok(albumDTO);
    }

}
