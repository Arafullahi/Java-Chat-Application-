package com.application.chat.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.application.chat.dto.ActionType;
import com.application.chat.entity.Message;
import com.application.chat.service.ChatMessagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.security.Principal;

@ExtendWith(MockitoExtension.class)
public class ChatGroupControllerTest {


        private final String topic = "/topic/test";
        @InjectMocks
        private ChatGroupController chatGroupController;
        @Mock
        private SimpMessageSendingOperations messagingTemplate;
        @Mock
        private ChatMessagesService chatMessagesService;
        @Mock
        SimpMessageHeaderAccessor headerAccessor;

        @BeforeEach
        public void setup() {


        }

        @Test
        public void testSendMessage_send_returns_success() {
            // Mock data
            Message chat = new Message();
            chat.setContent("Test message");
            chat.setSender("User");
            chat.setType(ActionType.CHAT);


            // Mock service behavior
            when(chatMessagesService.save(any())).thenReturn(chat);
            when(headerAccessor.getUser()).thenReturn(Mockito.mock(Principal.class));
            when(headerAccessor.getUser().getName()).thenReturn("TestUser");
            // Call controller method
            boolean success = chatGroupController.sendMessage(chat,headerAccessor);

            // Verify service method was called
            verify(chatMessagesService, times(1)).save(any());

            // Verify result
            assertTrue(success);
        }

        @Test
        public void testSendMessage_delete_returns_success() {
            // Mock data
            Message chat = new Message();
            chat.setContent("Test message");
            chat.setSender("User");
            chat.setType(ActionType.DELETE);

            // Mock service behavior
            when(chatMessagesService.deleteById(any())).thenReturn(Boolean.TRUE);
            when(headerAccessor.getUser()).thenReturn(Mockito.mock(Principal.class));
            when(headerAccessor.getUser().getName()).thenReturn("TestUser");
            // Call controller method
            boolean success = chatGroupController.sendMessage(chat, headerAccessor);

            // Verify service method was called
            verify(chatMessagesService, times(1)).deleteById(any());

            // Verify result
            assertTrue(success);
        }

    @Test
    public void JoinChat() {
        // Mock data
        Message message = new Message();
        message.setContent("Test message");
        message.setSender("User");
        message.setType(ActionType.JOIN);

        // Mock service behavior
        when(chatMessagesService.save(any())).thenReturn(message);
        when(headerAccessor.getUser()).thenReturn(Mockito.mock(Principal.class));
        when(headerAccessor.getUser().getName()).thenReturn("TestUser");
        // Call controller method
         chatGroupController.userJoin(message, headerAccessor);

        // Verify service method was called
        verify(chatMessagesService, times(1)).save(any());

    }
}

