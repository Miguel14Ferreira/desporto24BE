package com.example.desporto24;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

import java.io.File;

import static com.example.desporto24.constant.FileConstant.USER_FOLDER;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableScheduling
public class Desporto24Application {
    private static final Logger log = LoggerFactory.getLogger(Desporto24Application.class);

    public static void main(String[] args) throws InterruptedException {
        long startNanos = System.nanoTime();
        SpringApplication application = new SpringApplication(Desporto24Application.class);
        application.addListeners((ApplicationListener<ApplicationPreparedEvent>) event -> log.info("Startup checkpoint: context prepared after {} ms", elapsedMs(startNanos)));
        application.addListeners((ApplicationListener<ContextRefreshedEvent>) event -> log.info("Startup checkpoint: context refreshed after {} ms", elapsedMs(startNanos)));
        application.addListeners((ApplicationListener<ApplicationStartedEvent>) event -> log.info("Startup checkpoint: application started after {} ms", elapsedMs(startNanos)));
        application.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> log.info("Startup checkpoint: application ready after {} ms", elapsedMs(startNanos)));
        application.run(args);
        createDirectories(USER_FOLDER);
    }

    private static long elapsedMs(long startNanos) {
        return java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
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
