package com.application.chat.controller;


import com.application.chat.dto.MessageDto;
import com.application.chat.entity.Message;
import com.application.chat.exception.CustomNotFoundException;
import com.application.chat.service.ChatMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
@Slf4j
public class ChatController {

    @Autowired(required = true)
    private ChatMessagesService chatMessagesService;
    @GetMapping
    public  ResponseEntity<String> getStatus() {
        return ResponseEntity.accepted().body("success");
    }
    @GetMapping("/getMessageHistory/{group}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getChatMessage(
            @PathVariable("group") String group,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        log.info("getChatMessage called by group: {}",group);

        Pageable pagingSort = PageRequest.of(page, size, Sort.by("createdAt"));
        return ResponseEntity.ok().body(chatMessagesService.getMessageBYGroup(group, pagingSort));
    }

    @PostMapping("/sendMessage")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Deprecated
    public ResponseEntity<Object> sendMessage(@RequestBody MessageDto messageDto) {
        log.info("sendMessage called by sender: {}",messageDto);
        Message message = Message.builder()
                .sender(messageDto.sender())
                .groupName(messageDto.groupName())
                .content(messageDto.content())
                .type(messageDto.type())
                .build();
        return ResponseEntity.accepted().body(chatMessagesService.save(message));
    }

    @DeleteMapping("/deleteMessage/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Deprecated
    public ResponseEntity<Object> deleteMessage(@PathVariable BigInteger id) throws CustomNotFoundException {
        log.info("delteMessage called by id: {}",id);
        chatMessagesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}
