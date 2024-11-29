package com.example.desporto24.mapper;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.SessionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    Session toEntity(SessionDTO sessionDTO);
}
