package com.example.desporto24.service;

import com.example.desporto24.model.Ideias;
import com.example.desporto24.repository.IdeiasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ideiasService {

    @Autowired
    private IdeiasRepository ideiasRepository;

    public Ideias salvarIdeia(Ideias ideia){return ideiasRepository.save(ideia);}
}
