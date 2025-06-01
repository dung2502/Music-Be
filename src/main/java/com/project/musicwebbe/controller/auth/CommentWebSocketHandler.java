package com.project.musicwebbe.controller.auth;

import com.project.musicwebbe.entities.Comment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;



@Controller
public class CommentWebSocketHandler {

    @MessageMapping("/sendComment")
    @SendTo("/topic/createComment")
    public Comment sendAllComment(Comment comment){
        return comment ;
    }

    @MessageMapping("/detailComment")
    @SendTo("/topic/comment")
    public String sendComment(String message){
        return message ;
    }

}
