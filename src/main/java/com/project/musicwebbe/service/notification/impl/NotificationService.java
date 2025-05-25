package com.project.musicwebbe.service.notification.impl;

import com.project.musicwebbe.dto.notificationDTO.INotificationDTO;
import com.project.musicwebbe.dto.respone.AuthenticationResponse;
import com.project.musicwebbe.entities.Notification;
import com.project.musicwebbe.entities.UserNotification;
import com.project.musicwebbe.entities.permission.AppUser;
import com.project.musicwebbe.repository.NotificationRepository;
import com.project.musicwebbe.repository.UserNotificationRepository;
import com.project.musicwebbe.repository.permission.UserRepository;
import com.project.musicwebbe.service.notification.INotificationService;
import com.project.musicwebbe.service.permission.impl.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class NotificationService implements INotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  AuthenticationService authenticationService;
    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id).get();
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void remove(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendNotificationToAllUsers(Notification notification) {
        Notification notification1 = notificationRepository.save(notification);
        List<AppUser> users = userRepository.findAll();
        for (AppUser user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setAppUser(user);
            userNotification.setNotification(notification1);
            userNotificationRepository.save(userNotification);
        }
    }

    @Override
    public AuthenticationResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return authenticationService.getMyInfo(username);
    }

    @Override
    public boolean markAsRead(Long userId) {
        int updateRows = notificationRepository.markUnreadNotificationsAsRead(userId);
        return updateRows > 0;
    }

    @Override
    public int getUnreadNotificationCount(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    @Override
    public boolean markNotificationAsRead(Long userId, Long notificationId) {
        int updatedRows = notificationRepository.markNotificationAsRead(userId, notificationId);
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public boolean markAllNotificationsAsRead(Long userId) {
        int updatedRows = notificationRepository.markAllNotificationsAsRead(userId);
        return updatedRows > 0;
    }

    @Override
    public void sendNotificationToUser(Long userId, Notification notification) {
        Notification notification1 = notificationRepository.save(notification);
        // Tìm user theo userId
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo UserNotification
        UserNotification userNotification = new UserNotification();
        userNotification.setAppUser(user);
        userNotification.setNotification(notification1);

        // Lưu thông báo cho user cụ thể
        userNotificationRepository.save(userNotification);
    }

    @Override
    public List<INotificationDTO> findAllByRoleAndUser(Long roleId, Long userId) {
        return notificationRepository.findAllByRoleAndUser(roleId,userId);
    }

//    @Override
//    public void sendNotificationToAllUsers(Notification notification) {
//        notificationRepository.save(notification); // Lưu notification gốc
//
//        List<AppUser> users = userRepository.findAll(); // Lấy tất cả user
//
//        for (AppUser user : users) {
//            UserNotification userNotification = new UserNotification();
//            userNotification.setAppUser(user);
//            userNotification.setNotification(notification);
//            userNotification.setStatusRead(false);
//            userNotificationRepository.save(userNotification); // Lưu thông báo cho từng user
//        }
//    }


}
