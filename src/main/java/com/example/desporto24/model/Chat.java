package com.example.desporto24.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Chat {
    private Long id;
    private String username1;
    private String username2;
    private String nomeDoChat;
    private String chatUsername1;
    private String chatUsername2;

    public Chat(String username1, String username2, String nomeDoChat, String fraseUsername1, String fraseUsername2) {
        this.username1 = username1;
        this.username2 = username2;
        this.nomeDoChat = nomeDoChat;
        this.chatUsername1 = fraseUsername1;
        this.chatUsername2 = fraseUsername2;
    }
}
