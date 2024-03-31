package com.application.chat.service;

import com.application.chat.dto.MessageDto;
import com.application.chat.entity.Message;
import com.application.chat.exception.CustomNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;

public interface ChatMessagesService {
    final Pageable pageableWithSortByGroup = PageRequest.of(0, 2,
            Sort.by("createdAt"));

    Message save(Message message);

    Boolean deleteById(BigInteger messageId) throws CustomNotFoundException;

    Page<Message> getMessageBYGroup(String group, Pageable pageable);
}
