package com.example.desporto24.Chat;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ChatRequest {
    private String username1;
    private String username2;
    private String texto;
}