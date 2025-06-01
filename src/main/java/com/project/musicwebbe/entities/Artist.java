package com.project.musicwebbe.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long artistId;

    @NotBlank(message = "Tên nghệ sĩ không được để trống!")
    @Size(max = 100, message = "Tên nghệ sĩ không được vượt quá 100 ký tự!")
    @Column(name = "artist_name")
    private String artistName;

    @NotBlank(message = "Ảnh đại diện không được để trống!")
    @Size(max = 300, message = "URL ảnh đại diện không được vượt quá 300 ký tự!")
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "artist_status")
    private boolean artistStatus = false;

    @ManyToMany
    @JoinTable(
            name = "artist_genre",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Size(min = 1, message = "Nghệ sĩ phải thuộc ít nhất một thể loại!")
    private List<Genre> genres;

    @Size(max = 2000, message = "Tiểu sử không được vượt quá 2000 ký tự!")
    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @ManyToMany
    @JoinTable(
            name = "artist_song",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    @ManyToMany
    @JoinTable(
            name = "artist_album",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private List<Album> albums;

    @OneToMany(mappedBy = "artist")
    private List<FavoriteArtist> favoriteArtists;
}
