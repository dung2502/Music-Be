package com.project.musicwebbe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.musicwebbe.entities.permission.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @NotBlank(message = "Tên playlist không được để trống!")
    @Size(max = 100, message = "Tên playlist không được vượt quá 100 ký tự!")
    @Column(name = "playlist_name")
    private String playlistName;

    @NotBlank(message = "Mã playlist không được để trống!")
    @Size(max = 50, message = "Mã playlist không được vượt quá 50 ký tự!")
    private String playlistCode;

    @Size(max = 255, message = "URL ảnh bìa không được vượt quá 255 ký tự!")
    private String coverImageUrl;

    private boolean playlistStatus = false;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự!")
    @Column(name = "playList_desc", columnDefinition = "TEXT")
    private String description;

    @PastOrPresent(message = "Ngày tạo không hợp lệ!")
    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @NotNull(message = "Playlist phải gắn với một người dùng!")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayListSong> playListSongs;

    @OneToMany(mappedBy = "playlist", orphanRemoval = true)
    private List<FavoritePlaylist> favoritePlaylists;
}
