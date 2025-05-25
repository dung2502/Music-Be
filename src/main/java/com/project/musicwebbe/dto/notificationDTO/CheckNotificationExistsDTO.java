package com.project.musicwebbe.dto.notificationDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckNotificationExistsDTO {
    String title;
    String urlImage;
    String path;
    String content;
    List<Long> listRole;
}
