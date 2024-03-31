package com.application.chat.entity;

import com.application.chat.dto.ActionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.sql.Timestamp;


@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Message {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "type")
    private ActionType type;

    @Column(name = "content")
    private String content;

    @Column(name = "groupname")
    private String groupName;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
}
