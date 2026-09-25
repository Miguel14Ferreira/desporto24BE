package com.example.desporto24.mapper;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import org.mapstruct.Mapper;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toEntity(ProfileDTO profileDTO);

    ProfileDTO toDto(Profile profile);

    default String formatJoinDate(Date date) {
        return new java.text.SimpleDateFormat("yyyy MMM dd hh:mm").format(date);
    }
}
