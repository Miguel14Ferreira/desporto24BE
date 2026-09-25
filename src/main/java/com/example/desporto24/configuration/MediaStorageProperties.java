package com.example.desporto24.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "desporto.media")
public class MediaStorageProperties {
    private String provider = "local";
    private String localRoot = "./data/media";
    private String publicBaseUrl = "";
    private String s3Bucket = "";
    private String s3Region = "auto";
    private String s3Endpoint = "";
    private String s3AccessKey = "";
    private String s3SecretKey = "";
}
