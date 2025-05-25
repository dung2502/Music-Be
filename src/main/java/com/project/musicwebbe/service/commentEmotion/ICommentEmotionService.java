package com.project.musicwebbe.service.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentEmotion;
import com.project.musicwebbe.service.IGeneralService;

public interface ICommentEmotionService extends IGeneralService<CommentEmotion> {
    void removeEmotion(Long commentId, Long userId);
}