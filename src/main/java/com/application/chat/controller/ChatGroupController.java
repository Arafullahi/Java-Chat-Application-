package com.application.chat.controller;


import com.application.chat.entity.Message;
import com.application.chat.exception.CustomNotFoundException;
import com.application.chat.service.ChatMessagesService;
import com.application.chat.dto.ActionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatGroupController {
    @Value("${chat.websocket.topic}")
    private String topic;
    @Value(("${chat.websocket.default.group}"))
    private String defaultGroup;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private ChatMessagesService chatMessagesService;

    @MessageMapping("/send")
    public boolean sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Message Request");
        String user = headerAccessor.getUser().getName();
        message.setGroupName(defaultGroup);
        message.setSender(user);
        if (ActionType.CHAT.equals(message.getType())) {
            message = chatMessagesService.save(message);
        } else if (ActionType.DELETE.equals(message.getType())) {
            try {
                chatMessagesService.deleteById(message.getId());
            } catch (CustomNotFoundException e) {
                log.error("Messsage not found",e);
                //ToDo Group specific Posting

            }
        }

        this.messagingTemplate.convertAndSend(this.topic, message);
        //ToDO Sending back the Ack to user
        //ToDo Group specific Posting
        return true;
    }

    @MessageMapping("/join")
    public void userJoin(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Register Request");
        String user = headerAccessor.getUser().getName();
        message.setGroupName(defaultGroup);
        message.setSender(user);
        String sessionId = headerAccessor.getSessionId();
        log.info("Subscriber Start! User:{}, Session:{}", user, sessionId);
        headerAccessor.getSessionAttributes().put("user", user);
        message = chatMessagesService.save(message);
        this.messagingTemplate.convertAndSend(this.topic, message);
        //ToDO Sending back the Ack to user
        //ToDo Group specific Posting
    }
}
