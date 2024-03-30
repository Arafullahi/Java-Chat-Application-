package com.application.chat.service;

import com.application.chat.dto.MessageDto;
import com.application.chat.entity.Message;
import com.application.chat.exception.CustomNotFoundException;
import com.application.chat.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ChatMessagesServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatMessagesServiceImpl chatMessagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        MessageDto messageDto = new MessageDto("Test sender", "Test group","Test content");


        Message savedMessage = new Message();
        savedMessage.setId(BigInteger.ONE);
        savedMessage.setSender("Test sender");
        savedMessage.setGroupName("Test group");
        savedMessage.setContent("Test content");

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = chatMessagesService.save(messageDto);

        assertNotNull(result);
        assertEquals(BigInteger.ONE, result.getId());
        assertEquals("Test sender", result.getSender());
        assertEquals("Test group", result.getGroupName());
        assertEquals("Test content", result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testDeleteById() throws CustomNotFoundException {
        BigInteger messageId = BigInteger.ONE;
        Message message = new Message();
        message.setId(messageId);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        boolean result = chatMessagesService.deleteById(messageId);

        assertTrue(result);
        verify(messageRepository, times(1)).findById(messageId);
        verify(messageRepository, times(1)).deleteById(messageId);
    }

    @Test
    void testDeleteById_MessageNotFound() {
        BigInteger messageId = BigInteger.ONE;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(CustomNotFoundException.class, () -> chatMessagesService.deleteById(messageId));

        verify(messageRepository, times(1)).findById(messageId);
        verify(messageRepository, never()).deleteById(any());
    }

    @Test
    void testGetMessageBYGroup() {
        String group = "Test group";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Message> expectedPage = new PageImpl<>(Collections.emptyList());

        when(messageRepository.findByGroupName(eq(group), any())).thenReturn(expectedPage);

        Page<Message> result = chatMessagesService.getMessageBYGroup(group);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(messageRepository, times(1)).findByGroupName(eq(group), any());
    }
}
