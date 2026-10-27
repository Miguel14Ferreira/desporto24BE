package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(schema="desporto24", name="private_group_member",
       uniqueConstraints=@UniqueConstraint(name="uq_private_group_member",columnNames={"group_id","username"}))
@Getter @Setter
public class PrivateGroupMember {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="group_id", nullable=false)
    private Long groupId;
    @Column(nullable=false,length=128)
    private String username;
    @Column(name="joined_at",nullable=false)
    private Instant joinedAt;
}
