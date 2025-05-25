package com.project.musicwebbe.service.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentLike;
import com.project.musicwebbe.service.IGeneralService;

public interface ICommentLikeService extends IGeneralService<CommentLike> {
    void likeComment(Long commentId, Long userId);
}
