package com.example.desporto24.mapper;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.enumeration.Role;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Status;
import com.example.desporto24.model.dto.ProfileDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.*;
import java.util.Date;

@Mapper(componentModel = "spring", imports = {AES.class})
public interface ProfileMapper {

    @Mapping(target = "password", expression = "java(AES.encryptString(profileDTO.getPassword(), profileDTO.getUserId()))")
    @Mapping(target = "phone", expression = "java(AES.encryptString(profileDTO.getPhone(), profileDTO.getUserId()))")
    @Mapping(target = "address", expression = "java(AES.encryptString(profileDTO.getAddress(), profileDTO.getUserId()))")
    Profile toEntity(ProfileDTO profileDTO);

    @Mapping(target = "password", ignore = true) // Assuming you don't want to expose encrypted password
    @Mapping(target = "phone", expression = "java(AES.decryptString(profile.getPhone(), profile.getUserId()))")
    @Mapping(target = "address", expression = "java(AES.decryptString(profile.getAddress(), profile.getUserId()))")
    ProfileDTO toDto(Profile profile);

    default String formatJoinDate(Date date) {
        return new java.text.SimpleDateFormat("yyyy MMM dd hh:mm").format(date);
    }
}
