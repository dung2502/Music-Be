package com.project.musicwebbe.service.commentEmotion;

import com.project.musicwebbe.entities.commentEmotion.CommentHaha;
import com.project.musicwebbe.service.IGeneralService;

public interface ICommentHahaService extends IGeneralService<CommentHaha> {
    void hahaComment(Long commentId, Long userId);

}
