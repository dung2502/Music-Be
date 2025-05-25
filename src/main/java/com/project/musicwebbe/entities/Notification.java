package com.project.musicwebbe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notif_id")
    private Long notifId;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "title")
    private String title;

    @Column(name = "url_image")
    private String urlImage;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "path")
    private String path;

}
