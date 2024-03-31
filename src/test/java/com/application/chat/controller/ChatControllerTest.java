package com.application.chat.controller;

import com.application.chat.dto.ActionType;
import com.application.chat.dto.MessageDto;
import com.application.chat.entity.Message;
import com.application.chat.service.ChatMessagesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessagesService chatMessagesService;

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void testGetStatus() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("success"));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void testGetChatMessage() throws Exception {
        String group = "testGroup";
        Page<Message> messages = Mockito.mock(Page.class);
        //Mockito.when(chatMessagesServiceImpl.getMessageBYGroup(group)).thenReturn(messages);
        when(chatMessagesService.getMessageBYGroup(any(), any())).thenReturn(null);

        mockMvc.perform(get("/api/getMessageHistory/{group}", group))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void testSendMessage() throws Exception {
        MessageDto messageDto = new MessageDto("sende","group", "hi", ActionType.CHAT);


        when(chatMessagesService.save(any(Message.class))).thenReturn(new Message());

        mockMvc.perform(post("/api/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)

                        .content("{\"content\":\"Test content\",\"sender\":\"Test sender\",\"groupName\":\"Test sender\"}"))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    void testDeleteMessage() throws Exception {
        BigInteger id = BigInteger.ONE;
       when(chatMessagesService.deleteById(id)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/deleteMessage/{id}", id))
                .andExpect(status().isNoContent());
    }
}
