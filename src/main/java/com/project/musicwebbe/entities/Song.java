package com.project.musicwebbe.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Tên bài hát không được để trống!")
    @Column(name = "lyrics",columnDefinition = "TEXT")
    private String lyrics;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @NotBlank(message = "Bài hát không được để trống!")
    private String songUrl;


    @Min(value = 1, message = "Thời lượng phải lớn hơn 0!")
    private int duration;

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
    @NotEmpty(message = "Thể loại không được để trống!")
    private List<Genre> genres;

    @NotEmpty(message = "Ca sĩ không được để trống!")
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