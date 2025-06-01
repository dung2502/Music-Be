package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.dto.songDTO.SongDTO;
import com.project.musicwebbe.entities.*;
import com.project.musicwebbe.service.song.impl.SongService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/public/songs")
public class SongRestController {
    @Autowired
    private SongService songService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @GetMapping("/suggestedSongs")
    public ResponseEntity<Page<SongDTO>> getAllSuggestedSongs() {
        PageRequest pageRequest = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "dateCreate"));
        Page<Song> songs = songService.findAll(pageRequest);
        Page<SongDTO> songDTOS = songs.map(convertEntityToDTO::convertToSongDTO);

        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/top-3-songs-in-7-days")
    public ResponseEntity<List<SongDTO>> getTopThreeSongsInSevenDays() {
        List<Song> songs = songService.findTopThreeSongsInSevenDays();
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<SongDTO> songDTOS = songs.stream().map(convertEntityToDTO::convertToSongDTO).toList();
        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/top-song")
    public ResponseEntity<Page<SongDTO>> getTopSongs(@RequestParam(name = "national", defaultValue = "") String national,
                                                     @RequestParam(name = "size", defaultValue = "100") int size
    ) {
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Song> songs = songService.findAllTopSongByNational(national, pageRequest);
        Page<SongDTO> songDTOS = songs.map(convertEntityToDTO::convertToSongDTO);
        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/new-song-ratings")
    public ResponseEntity<List<SongDTO>> getNewSongRatings() {
        List<Song> tempSongs = songService.findNewSongRatings();
        if (tempSongs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<SongDTO> songs = tempSongs.stream().map(convertEntityToDTO::convertToSongDTO).toList();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/new-songs-release")
    public ResponseEntity<List<SongDTO>> getNewSongsRelease(@RequestParam(name = "national", defaultValue = "") String national
    ) {
        List<Song> songs = songService.findNewSongsWithNational(national);
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<SongDTO> songDTOS = songs.stream().map(convertEntityToDTO::convertToSongDTO).toList();
        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/top-100-songs")
    public ResponseEntity<List<SongDTO>> getTop100Songs() {
        List<Song> tempSongs = songService.findTop100Songs();
        if (tempSongs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<SongDTO> songs = tempSongs.stream().map(convertEntityToDTO::convertToSongDTO).toList();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{songId}")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long songId) {
        Song song = songService.findById(songId);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertEntityToDTO.convertToSongDTO(song));
    }

    @GetMapping("/top-6-most-listened")
    public ResponseEntity<Page<SongDTO>> getTopSixMostListenedSongs() {
        PageRequest pageRequest = PageRequest.of(0, 6);
        Page<Song> songs = songService.findSixSongBestListening(pageRequest);
        Page<SongDTO> songDTOS = songs.map(convertEntityToDTO::convertToSongDTO);
        return ResponseEntity.ok(songDTOS);
    }

    @GetMapping("/song-new-released")
    public ResponseEntity<Page<SongDTO>> getSongNewReleased() {
        PageRequest pageRequest = PageRequest.of(0, 9);
        Page<Song> songs = songService.findSongNewReleased(pageRequest);
        Page<SongDTO> songDTOS = songs.map(convertEntityToDTO::convertToSongDTO);
        return ResponseEntity.ok(songDTOS);
    }

}
