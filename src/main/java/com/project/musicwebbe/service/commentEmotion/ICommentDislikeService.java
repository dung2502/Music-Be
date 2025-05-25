package com.project.musicwebbe.service.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentDislike;
import com.project.musicwebbe.service.IGeneralService;

public interface ICommentDislikeService extends IGeneralService<CommentDislike> {
    void dislikeComment(Long commentId, Long userId);
}
