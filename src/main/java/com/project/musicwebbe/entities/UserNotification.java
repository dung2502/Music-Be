package com.project.musicwebbe.entities;

import com.project.musicwebbe.entities.permission.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_notification")
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean statusRead = false; // Trạng thái đã đọc

    @ManyToOne
    @JoinColumn(name = "notif_id")
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

}