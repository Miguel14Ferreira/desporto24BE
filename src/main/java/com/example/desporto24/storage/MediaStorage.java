package com.example.desporto24.storage;

import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

public interface MediaStorage {
    URI store(String key, MultipartFile file);
    void delete(String key);
}
