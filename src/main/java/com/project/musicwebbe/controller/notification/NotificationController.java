package com.project.musicwebbe.controller.notification;

import com.project.musicwebbe.dto.notificationDTO.INotificationDTO;
import com.project.musicwebbe.dto.respone.ErrorDetail;
import com.project.musicwebbe.entities.Notification;
import com.project.musicwebbe.service.notification.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/notifications")
public class NotificationController {
    @Autowired
    private INotificationService notificationService;
    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;
    @GetMapping("/list")
    public ResponseEntity<?> getNotificationsByRoleAndUser(
            @RequestParam Long roleId,
            @RequestParam Long userId) {
        List<INotificationDTO> notifications = notificationService.findAllByRoleAndUser(roleId, userId);
        return ResponseEntity.ok(notifications);
    }
    // Cập nhật trạng thái tất cả các thông báo cho user cụ thể
    @PatchMapping("/markAllAsRead")
    public ResponseEntity<?> markAllNotificationsAsRead(@RequestParam Long userId) {
        try {
            boolean updatedCount = notificationService.markAllNotificationsAsRead(userId);
            return ResponseEntity.ok("Đã cập nhật " + updatedCount + " thông báo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }


    // Cập nhật trạng thái của một thông báo cụ thể
    @PutMapping("/markAsRead")
    public ResponseEntity<Void> markNotificationAsRead(
            @RequestParam Long userId,
            @RequestParam Long notificationId) {
        boolean success = notificationService.markNotificationAsRead(userId, notificationId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.status(500).build();
    }

    // Gửi thông báo đến tất cả người dùng
    @PostMapping("/sendToAll")
    public ResponseEntity<Void> sendNotificationToAll(@RequestBody Notification notification) {
        notificationService.sendNotificationToAllUsers(notification);
        return ResponseEntity.ok().build();
    }

    // Gửi thông báo đến người dùng cụ thể
    @PostMapping("/sendToUser")
    public ResponseEntity<Void> sendNotificationToUser(
            @RequestParam Long userId,
            @RequestBody Notification notification) {
        notificationService.sendNotificationToUser(userId, notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNotification(@Validated @RequestBody Notification notification, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorDetail errors = new ErrorDetail("Validation errors");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        notificationService.save(notification);
        return ResponseEntity.ok(200);
    }

}
