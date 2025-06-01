package com.project.musicwebbe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlist_song")
public class PlayListSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "date_add")
    private LocalDateTime dateAdd;

    @PrePersist
    protected void onCreate() {
        if (dateAdd == null) {
            dateAdd = LocalDateTime.now();
        }
    }

}
