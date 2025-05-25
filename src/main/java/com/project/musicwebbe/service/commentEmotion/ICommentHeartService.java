package com.project.musicwebbe.service.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentHeart;
import com.project.musicwebbe.service.IGeneralService;

public interface ICommentHeartService extends IGeneralService<CommentHeart> {
    void heartComment(Long commentId, Long userId);

}
