package com.example.desporto24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

import java.io.File;

import static com.example.desporto24.constant.FileConstant.USER_FOLDER;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class Desporto24Application {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Desporto24Application.class, args);
        createDirectories(USER_FOLDER);
    }

    private static void createDirectories(String... directories) {
        for (String directory : directories) {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

}
