package com.project.musicwebbe.controller.publicController;

import com.project.musicwebbe.dto.albumDTO.ArtistOfAlbumDTO;
import com.project.musicwebbe.dto.searchDTO.ArtistOfSearch;
import com.project.musicwebbe.dto.searchDTO.SearchDTO;
import com.project.musicwebbe.dto.songDTO.AlbumOfSongDTO;
import com.project.musicwebbe.dto.songDTO.ArtistOfSongDTO;
import com.project.musicwebbe.dto.songDTO.SongDTO;
import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.entities.Artist;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.SongListen;
import com.project.musicwebbe.service.album.IAlbumService;
import com.project.musicwebbe.service.artist.IArtistService;
import com.project.musicwebbe.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/search")
public class SearchRestController {

    @Autowired
    private IAlbumService albumService;

    @Autowired
    private IArtistService artistService;

    @Autowired
    private ISongService songService;

    @GetMapping
    public ResponseEntity<List<SearchDTO>> searchAll(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<Album> albums = albumService.findByAlbumName(keyword);
        List<Artist> artists = artistService.searchAllByArtistName(keyword);
        List<Song> songs = songService.searchAllByTitle(keyword);

        List<SearchDTO> searchDTOs = new ArrayList<>();
        List<SearchDTO> searchAlbums;
        List<SearchDTO> searchSongs;
        List<SearchDTO> searchArtists;
        searchAlbums = albums.stream()
                .map(album -> SearchDTO.builder()
                        .searchId(album.getAlbumId())
                        .searchTitle(album.getTitle())
                        .coverImageUrl(album.getCoverImageUrl())
                        .artists(album.getArtists().stream()
                                .map(artist ->
                                     ArtistOfSearch.builder()
                                            .artistId(artist.getArtistId())
                                            .artistName(artist.getArtistName())
                                            .avatar(artist.getAvatar())
                                            .build()).toList())
                        .type("album").build()).toList();
        searchSongs = songs.stream()
                .map(song -> SearchDTO.builder()
                        .searchId(song.getSongId())
                        .searchTitle(song.getTitle())
                        .coverImageUrl(song.getCoverImageUrl())
                        .artists(song.getArtists().stream()
                                .map(artist -> ArtistOfSearch.builder()
                                        .artistId(artist.getArtistId())
                                        .artistName(artist.getArtistName())
                                        .avatar(artist.getAvatar())
                                        .build()).toList())
                        .type("song").build()
                ).toList();
        searchArtists = artists.stream()
                .map(artist -> SearchDTO.builder()
                        .searchId(artist.getArtistId())
                        .searchTitle(artist.getArtistName())
                        .coverImageUrl(artist.getAvatar())
                        .type("artist").build()).toList();
        searchDTOs.addAll(searchAlbums);
        searchDTOs.addAll(searchSongs);
        searchDTOs.addAll(searchArtists);

        return ResponseEntity.ok(searchDTOs);
    }


}
