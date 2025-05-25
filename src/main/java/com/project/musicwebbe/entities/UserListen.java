package com.project.musicwebbe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.musicwebbe.entities.permission.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_listens")
public class UserListen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listen_id")
    private Long listensId;

    @Column(name = "listened_at", nullable = false)
    private LocalDate listenedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;
}
