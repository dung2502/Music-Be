package com.project.musicwebbe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Size(max = 100, message = "Tiêu đề không được vượt quá 100 ký tự!")
    private String title;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @NotBlank(message = "Ảnh bìa không được để trống!")
    @Size(max = 255, message = "URL ảnh bìa không được vượt quá 255 ký tự!")
    private String coverImageUrl;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    private List<Song> songs;

    @Size(max = 100, message = "Thông tin nhà cung cấp không được vượt quá 100 ký tự!")
    private String provide;

    @Column(name = "album_status")
    private boolean albumStatus = false;

    @ManyToMany()
    @JoinTable(
            name = "artist_album",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    @NotEmpty(message = "Phải chọn ít nhất một nghệ sĩ!")
    private List<Artist> artists;

    @ManyToMany()
    @JoinTable(
            name = "album_genre",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @NotEmpty(message = "Phải chọn ít nhất một thể loại!")
    private List<Genre> genres;

    @OneToMany(mappedBy = "album")
    private List<FavoriteAlbum> favoriteAlbums;
}
