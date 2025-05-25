package com.project.musicwebbe.service.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentWow;
import com.project.musicwebbe.service.IGeneralService;

public interface ICommentWowService extends IGeneralService<CommentWow> {
    void wowComment(Long commentId, Long userId);

}
