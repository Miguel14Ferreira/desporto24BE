package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(schema="desporto24", name="private_group")
@Getter @Setter
public class PrivateGroup {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=90)
    private String title;
    @Column(name="photo_url",length=2048)
    private String photoUrl;
    @Column(name="creator_username", nullable=false, length=128)
    private String creatorUsername;
    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}
