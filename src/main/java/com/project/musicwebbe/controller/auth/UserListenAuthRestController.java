package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.dto.songDTO.UserListenDTO;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.entities.UserListen;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.service.permission.IUserService;
import com.project.musicwebbe.service.song.IUserListenService;
import com.project.musicwebbe.service.song.impl.SongService;
import com.project.musicwebbe.util.ConvertEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/user-listens")
public class UserListenAuthRestController {

    @Autowired
    private IUserListenService userListenService;

    @Autowired
    private ConvertEntityToDTO convertEntityToDTO;

    @Autowired
    private SongService songService;

    @Autowired
    private IUserService userService;

    @GetMapping("/recent/{userId}")
    public ResponseEntity<List<UserListenDTO>> getTop10RecentListens(@PathVariable Long userId) {
        List<UserListen> listens = userListenService.findTop10RecentByUserId(userId);
        if (listens.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UserListenDTO> listenDTOS = listens.stream()
                .map(convertEntityToDTO::convertUserListenDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listenDTOS);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserListen(@RequestParam Long songId, @RequestParam Long userId) {
        Song song = songService.findById(songId);
        if (song == null) {
            return ResponseEntity.status(404).body("Song not found");
        }

        AppUser user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        LocalDate today = LocalDate.now();
        UserListen userListen = userListenService.findBySongIdAndUserIdAndDate(songId, userId, today);

        if (userListen == null) {
            UserListen newUserListen = UserListen.builder()
                    .song(song)
                    .appUser(user)
                    .listenedAt(today)
                    .build();
            userListenService.save(newUserListen);
        }

        return ResponseEntity.ok().build();
    }

}
