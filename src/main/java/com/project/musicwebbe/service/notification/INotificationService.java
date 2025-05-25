package com.project.musicwebbe.service.notification;

import com.project.musicwebbe.dto.notificationDTO.INotificationDTO;
import com.project.musicwebbe.dto.respone.AuthenticationResponse;
import com.project.musicwebbe.entities.Notification;
import com.project.musicwebbe.service.IGeneralService;

import java.util.List;

public interface INotificationService extends IGeneralService<Notification> {
    void sendNotificationToAllUsers(Notification notification);
    AuthenticationResponse getCurrentUser();
    boolean markAsRead(Long userId);
    int getUnreadNotificationCount(Long userId);
    boolean markNotificationAsRead(Long userId, Long notificationId);
    boolean markAllNotificationsAsRead(Long userId);
    void sendNotificationToUser(Long userId, Notification notification);
    List<INotificationDTO> findAllByRoleAndUser(Long roleId, Long userId);
}
