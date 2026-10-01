package com.example.desporto24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

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

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(getAllowedOrigins());
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedHeaders(getAllowedHeaders());
        corsConfiguration.setExposedHeaders(getExposedHeaders());
        corsConfiguration.setAllowedMethods(getAllowedMethods());

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    private List<String> getAllowedOrigins() {
        return List.of(
                "http://localhost:4200",
                "http://localhost:3000",
                "http://desporto24.eu-north-1.elasticbeanstalk.com"
        );
    }

    private List<String> getAllowedHeaders() {
        return Arrays.asList(
                "Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept",
                "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"
        );
    }

    private List<String> getExposedHeaders() {
        return Arrays.asList(
                "Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
        );
    }

    private List<String> getAllowedMethods() {
        return Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        );
    }
}
