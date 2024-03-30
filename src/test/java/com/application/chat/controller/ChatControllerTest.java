package com.application.chat.controller;

import com.application.chat.entity.Message;
import com.application.chat.repository.MessageRepository;
import com.application.chat.service.ChatMessagesService;
import com.application.chat.service.ChatMessagesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Base64Utils;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "user1", password = "user1", roles = "USER")
@ContextConfiguration(classes = {ChatMessagesServiceImpl.class, ChatControllerTest.FooTestConfig.class})
public class ChatControllerTest {

    @TestConfiguration
    static class FooTestConfig {
        @Bean
        public ChatMessagesServiceImpl getChatMessagesServiceImpl() {
            return Mockito.mock(ChatMessagesServiceImpl.class);
        }

        @Bean
        public MessageRepository getMessageRepository() {
            return Mockito.mock(MessageRepository.class);
        }
    }
    @Mock
    private Logger logger;
    @Mock
    ChatMessagesServiceImpl chatMessagesServiceImpl;

    @Autowired
    private MockMvc mockMvc;


//    @InjectMocks
//    ChatController chatController;
    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("success"));
    }

    @Test

    public void testGetChatMessage() throws Exception {
        String group = "group1";
        Page<Message> messages = Mockito.mock(Page.class);
        Mockito.when(chatMessagesServiceImpl.getMessageBYGroup(group)).thenReturn(messages);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/getMessageHistory/{group}", group)
                        .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("user1:user1".getBytes()))
                        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }
}
