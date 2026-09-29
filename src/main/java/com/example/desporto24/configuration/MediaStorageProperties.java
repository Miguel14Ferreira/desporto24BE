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
    private S3 s3 = new S3();

    @Getter
    @Setter
    public static class S3 {
        private String bucket = "";
        private String region = "auto";
        private String endpoint = "";
        private String accessKey = "";
        private String secretKey = "";
    }
}
