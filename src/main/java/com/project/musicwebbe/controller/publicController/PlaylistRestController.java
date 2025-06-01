package com.project.musicwebbe.controller.publicController;


import com.project.musicwebbe.dto.playlistDTO.PlaylistDTO;
import com.project.musicwebbe.dto.playlistDTO.SongOfPlaylistDTO;
import com.project.musicwebbe.dto.playlistDTO.UserOfPlaylistDTO;
import com.project.musicwebbe.dto.songDTO.AlbumOfSongDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import com.project.musicwebbe.dto.songDTO.SongDTO;
import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.entities.PlayListSong;
import com.project.musicwebbe.entities.Playlist;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IUserService;
import com.project.musicwebbe.service.playlist.impl.PlaylistService;
import com.project.musicwebbe.service.song.impl.SongService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/playlists")
public class PlaylistRestController {

    @Autowired
    private PlaylistService playlistService;


    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylist(){
        List<Playlist> playlists = playlistService.findAll();
        if (playlists.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<PlaylistDTO> playlistDTOS = playlists.stream()
                .map(convertEntityToDTO::convertToPlaylistDTO)
                .collect(Collectors.toList());
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

}
