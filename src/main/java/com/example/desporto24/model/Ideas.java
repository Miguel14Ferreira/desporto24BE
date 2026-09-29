package com.example.desporto24.model;

import com.example.desporto24.enumeration.IdeaStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Ideas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotEmpty(message = "O campo nome não pode estar vazio")
    private String name;

    private String city;

    @NotEmpty(message = "O campo email não pode estar vazio")
    private String email;

    private String gender;
    private String subject;

    @Column(columnDefinition = "text")
    private String problem;

    @Enumerated(EnumType.STRING)
    @Column
    private IdeaStatus status = IdeaStatus.NEW;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public Ideas(String name, String email, String city, String gender, String subject, String problem) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.gender = gender;
        this.subject = subject;
        this.problem = problem;
        this.status = IdeaStatus.NEW;
        this.createdAt = Instant.now();
    }
}
