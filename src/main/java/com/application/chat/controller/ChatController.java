package com.application.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final Logger log = LoggerFactory.getLogger(ChatController.class);
    @GetMapping
    public  ResponseEntity<String> getStatus() {
        return ResponseEntity.accepted().body("success");
    }
    @GetMapping("/getMessageHistory/{group}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> getChatMessage(@PathVariable("group") String group) {
        log.info("getChatMessage called by group: {}",group);
        return ResponseEntity.ok().body(List.of());
    }

}
