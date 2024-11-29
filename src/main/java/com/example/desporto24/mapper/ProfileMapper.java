package com.example.desporto24.mapper;


import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toEntity(ProfileDTO profileDTO);
    ProfileDTO toDto(Profile Profile);
}
