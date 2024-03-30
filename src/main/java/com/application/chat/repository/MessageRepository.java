package com.application.chat.repository;

import com.application.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.math.BigInteger;

public interface MessageRepository extends ListPagingAndSortingRepository<Message, BigInteger>, JpaRepository<Message, BigInteger> {


    Page<Message> findByGroupName(String groupName, Pageable pageableWithSortByGroup);
}
