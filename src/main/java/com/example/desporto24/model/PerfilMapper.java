package com.example.desporto24.model;

import org.springframework.beans.BeanUtils;

public class PerfilMapper {
    public static Perfil fromUser(Perfil perfil){
        Perfil p = new Perfil();
        BeanUtils.copyProperties(perfil, p);
        return p;
    }
}
