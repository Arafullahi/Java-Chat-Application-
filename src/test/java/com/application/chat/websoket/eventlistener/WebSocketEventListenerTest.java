package com.application.chat.websoket.eventlistener;

import com.application.chat.dto.ActionType;
import com.application.chat.entity.Message;
import com.application.chat.service.ChatMessagesService;
import com.application.chat.wesocket.eventlistener.WebSocketEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketEventListenerTest {

    @Mock
    private SimpMessageSendingOperations messagingTemplate;

    @Mock
    private ChatMessagesService chatMessagesService;

    @InjectMocks
    private WebSocketEventListener webSocketEventListener;

    private SessionDisconnectEvent disconnectEvent;

    @BeforeEach
    public void setup() {
        disconnectEvent = mock(SessionDisconnectEvent.class);
    }

    @Test
    public void testHandleWebSocketDisconnect() {
        // Mock session attributes
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("user", "testUser");

        // Mock StompHeaderAccessor
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        headerAccessor.setSessionAttributes(sessionAttributes);

        // Mock Message object
        Message message = Message.builder()
                .type(ActionType.LEAVE)
                .sender("testUser")
                .groupName("defaultGroup")
                .build();

        // Mock the behavior of the ChatMessagesService
        when(chatMessagesService.save(any(Message.class))).thenReturn(message);
        try (MockedStatic<StompHeaderAccessor> mocked = mockStatic(StompHeaderAccessor.class)) {
            mocked.when(() -> StompHeaderAccessor.wrap(any())).thenReturn(headerAccessor);
            // Trigger the event listener
            webSocketEventListener.handleWebSocketDisconnect(disconnectEvent);
        }
        // Verify that the message is sent with the correct parameters
        verify(messagingTemplate, times(1)).convertAndSend(null, message);
    }
}

