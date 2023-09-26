package com.example.desporto24.newIdea;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NewIdeaRequest {
    @NotEmpty(message = "O campo nome não pode estar vazio")
    private String name;
    private String age;
    private String city;
    @NotEmpty(message = "O campo email não pode estar vazio")
    private String email;
    private String gender;
    private String subject;
    private String problem;
}
