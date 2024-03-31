package com.application.chat.wesocket.eventlistener;


import com.application.chat.entity.Message;
import com.application.chat.dto.ActionType;
import com.application.chat.service.ChatMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private ChatMessagesService chatMessagesService;
    @Value("${chat.websocket.topic}")
    private String topic;
    @Value(("${chat.websocket.default.group}"))
    private String defaultGroup;


    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String user = String.valueOf(headerAccessor.getSessionAttributes().get("user"));
        if (user == null || user.length() == 0)
            user = "Unknown User";
        Message chat = Message.builder()
                .type(ActionType.LEAVE)
                .sender(user)
                .groupName(defaultGroup)
                .build();
        chat = chatMessagesService.save(chat);
        messagingTemplate.convertAndSend(this.topic, chat);
    }
}
