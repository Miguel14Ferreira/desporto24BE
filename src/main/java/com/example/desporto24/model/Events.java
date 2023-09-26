package com.example.desporto24.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;

import jakarta.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Unique
    private String[] type;
    private String description;

}
