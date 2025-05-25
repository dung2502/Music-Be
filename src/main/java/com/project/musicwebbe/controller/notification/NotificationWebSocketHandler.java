package com.project.musicwebbe.controller.notification;

import com.project.musicwebbe.entities.Album;
import com.project.musicwebbe.entities.Notification;
import com.project.musicwebbe.entities.Song;
import com.project.musicwebbe.service.notification.INotificationService;
import com.project.musicwebbe.service.permission.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class NotificationWebSocketHandler {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private INotificationService notificationService;

    @MessageMapping("/notifyUser")
    public void sendNotificationToUser(@Payload Notification notification, Long userId) {
        String username = userService.findById(userId).getEmail();
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
    }

    @MessageMapping("/notifyAll")
    public void sendNotificationToAll(@Payload Notification notification) {
        simpMessagingTemplate.convertAndSend("/topic/public", notification);
    }
    @MessageMapping("/sendNoti-album")
    @SendTo("/topic/noti-album")
    public Notification createNotificationByAlbum(@Payload Notification notification) {
        notification.setCreateDate(LocalDateTime.now());
        notificationService.sendNotificationToAllUsers(notification);
        return notification;
    }

    @MessageMapping("/sendNoti-song")
    @SendTo("/topic/noti-song")
    public Notification createNotificationBySong(@Payload Song song)
    {
        Notification notification = new Notification();
        notification.setTitle(song.getTitle());
        notification.setUrlImage(song.getCoverImageUrl());
        notification.setContent(song.getLyrics());
        notification.setPath("songs/"+song.getSongId().toString());
        notification.setCreateDate(LocalDateTime.now());
        notificationService.sendNotificationToAllUsers(notification);
        return notification;
    }
}
