package com.project.musicwebbe.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sound.midi.Track;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long songId;

    @NotBlank(message = "Tên bài hát không được để trống!")
    private String title;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "lyrics",columnDefinition = "TEXT")
    private String lyrics;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @NotBlank(message = "Bài hát không được để trống!")
    private String songUrl; // URL tới file nhạc

    private int duration; // Thời lượng bài hát tính bằng giây

    @NotBlank(message = "Ảnh đại diện không được để trống!")
    private String coverImageUrl;

    @OneToMany(mappedBy = "song")
    private List<Favorite> favorites;

    @ManyToMany()
    @JoinTable(
            name = "song_genre",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany()
    @JoinTable(
            name = "artist_song",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;

    @OneToMany(mappedBy = "song")
    private List<SongListen> songListens;

    @OneToMany(mappedBy = "song")
    private List<UserListen> userListens;
}