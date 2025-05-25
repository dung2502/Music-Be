package com.project.musicwebbe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @NotBlank(message = "Tiêu đề không được để trống!")
    private String title;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @NotBlank(message = "Ảnh bìa không được để trống!")
    private String coverImageUrl; // URL tới ảnh bìa album

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private List<Song> songs;

    @Column(name = "provide")
    private String provide;

    @Column(name = "album_status")
    private boolean albumStatus = false;


    @ManyToMany()
    @JoinTable(
            name = "artist_album",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;

    @ManyToMany()
    @JoinTable(
            name = "album_genre",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @OneToMany(mappedBy = "album")
    private List<FavoriteAlbum> favoriteAlbums;
}