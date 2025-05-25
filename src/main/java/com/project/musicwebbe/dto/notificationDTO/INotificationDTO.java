package com.project.musicwebbe.dto.notificationDTO;

public interface INotificationDTO {
    Long getNotifId();
    String getTitle();
    String getCreateDate();
    String getContent();
    String getPath();
    String getUrlImage();
    Boolean getStatusRead();
}
