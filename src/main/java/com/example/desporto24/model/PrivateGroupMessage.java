package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(schema="desporto24",name="private_group_message")
@Getter @Setter
public class PrivateGroupMessage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="group_id",nullable=false)
    private Long groupId;
    @Column(name="sender_username",nullable=false,length=128)
    private String senderUsername;
    @Column(nullable=false,length=2000)
    private String body;
    @Column(name="created_at",nullable=false)
    private Instant createdAt;
}
