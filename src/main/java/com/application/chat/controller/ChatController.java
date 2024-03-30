package com.application.chat.controller;


import com.application.chat.dto.MessageDto;
import com.application.chat.exception.CustomNotFoundException;
import com.application.chat.service.ChatMessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired(required = true)
    ChatMessagesService chatMessagesService;
    @GetMapping
    public  ResponseEntity<String> getStatus() {
        return ResponseEntity.accepted().body("success");
    }
    @GetMapping("/getMessageHistory/{group}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getChatMessage(@PathVariable("group") String group) {
        log.info("getChatMessage called by group: {}",group);

        return ResponseEntity.ok().body(chatMessagesService.getMessageBYGroup(group));
    }

    @PostMapping("/sendMessage")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> sendMessage(@RequestBody MessageDto messageDto) {
        log.info("sendMessage called by sender: {}",messageDto);
        return ResponseEntity.accepted().body(chatMessagesService.save(messageDto));
    }

    @DeleteMapping("/deleteMessage/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteMessage(@PathVariable BigInteger id) throws CustomNotFoundException {
        log.info("delteMessage called by id: {}",id);
        chatMessagesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}
