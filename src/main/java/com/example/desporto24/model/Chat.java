package com.example.desporto24.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.lang.annotation.Documented;
import java.util.Date;
import java.util.function.Function;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Getter
@Setter
@Builder
public class Chat {
    @SequenceGenerator( name = "chat_sequence",
            sequenceName = "chat_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "chat_sequence")
    private Long id;
    private String username1;
    private String username2;
    private String texto;
    private String dataDisplay;
    private Date date;

    public Chat(String username1, String username2, String texto, String dataDisplay,Date date) {
        this.username1 = username1;
        this.username2 = username2;
        this.texto = texto;
        this.dataDisplay = dataDisplay;
        this.date = date;
    }

    public Chat(String username1, String username2, String texto) {
        this.username1 = username1;
        this.username2 = username2;
        this.texto = texto;
    }
}
