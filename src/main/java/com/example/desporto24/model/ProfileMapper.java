package com.example.desporto24.model;

import org.springframework.beans.BeanUtils;

public class ProfileMapper {
    public static Profile fromUser(Profile profile){
        Profile p = new Profile();
        BeanUtils.copyProperties(profile, p);
        return p;
    }
}
