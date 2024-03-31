package com.application.chat.service;

import com.application.chat.dto.MessageDto;
import com.application.chat.entity.Message;
import com.application.chat.exception.CustomNotFoundException;
import com.application.chat.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;



@Service
@Slf4j
public class ChatMessagesServiceImpl implements ChatMessagesService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        log.info("saving message:{}",message);

        message = messageRepository.save(message);
        log.info(" message saved:{}",message);
        return  message;
    }

    @Override
    public Boolean deleteById(BigInteger messageId) throws CustomNotFoundException {
        log.info("message to delete id:{}",messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(String.format("Record not found for deleteid %s", messageId)));
        log.info("message found for delete id:{}",messageId);
        messageRepository.deleteById(messageId);
        log.info("message  deleted id:{}",messageId);
        return Boolean.TRUE;
    }

    @Override
    public Page<Message> getMessageBYGroup(String group, Pageable pageable) {
        log.info("getMessageBYGroup group:{}",group);
        return messageRepository.findByGroupName(group,pageable);
    }



}
