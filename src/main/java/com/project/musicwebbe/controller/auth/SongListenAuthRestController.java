package com.project.musicwebbe.controller.auth;


import com.project.musicwebbe.entities.SongListen;
import com.project.musicwebbe.service.song.ISongListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/auth/SongListens")
public class SongListenAuthRestController {

    @Autowired
    private ISongListenService songListenService;

    @GetMapping("findAll")
    public ResponseEntity<List<SongListen>> getAllSongListens() {
        List<SongListen> songListens = songListenService.findAll();
        if (songListens.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songListens);
    }

}
