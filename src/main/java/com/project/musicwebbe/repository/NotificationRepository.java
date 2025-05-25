package com.project.musicwebbe.repository;

import com.project.musicwebbe.dto.notificationDTO.INotificationDTO;
import com.project.musicwebbe.entities.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query(value = """

            SELECT n.*, u.status_read as statusRead
                 FROM notification n
                 JOIN user_notification u ON n.notif_id = u.notif_id
                 JOIN app_users a ON a.user_id = u.user_id
                 JOIN user_role ur ON ur.user_id = a.user_id
                 WHERE ur.role_id = :roleId AND u.user_id = :userId
                 ORDER BY n.create_date DESC
            """, nativeQuery = true)
    List<INotificationDTO> findAllByRoleAndUser(@Param("roleId") Long roleId, @Param("userId") Long userId);


    @Modifying
    @Query("UPDATE UserNotification un SET un.statusRead = true WHERE un.appUser.userId = :userId AND un.statusRead = false")
    int markUnreadNotificationsAsRead(@Param("userId") Long userId);

    // Đếm số lượng thông báo chưa đọc (statusRead = false) cho một user nhất định
    @Query("SELECT COUNT(n) FROM UserNotification n WHERE n.appUser.userId = :userId AND n.statusRead = false")
    int countUnreadNotifications(@Param("userId") Long userId);

    // Cập nhật trạng thái statusRead của thông báo theo userId và notificationId
    @Modifying
    @Query("UPDATE UserNotification n SET n.statusRead = true WHERE n.appUser.userId = :userId AND n.notification.notifId = :notificationId")
    int markNotificationAsRead(@Param("userId") Long userId, @Param("notificationId") Long notificationId);

    // Cập nhật trạng thái của tất cả thông báo cho một user thành đã đọc
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_notification SET status_read = 1 WHERE user_id = :userId AND status_read = 0", nativeQuery = true)
    int markAllNotificationsAsRead(@Param("userId") Long userId);

}