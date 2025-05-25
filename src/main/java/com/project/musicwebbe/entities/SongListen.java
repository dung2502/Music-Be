package com.project.musicwebbe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "song_listens")
public class SongListen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listen_id")
    private Long listensId;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "total")
    private int total;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "song_id")
    private Song song;
}
