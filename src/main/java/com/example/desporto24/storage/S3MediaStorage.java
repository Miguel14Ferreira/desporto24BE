package com.example.desporto24.storage;

import com.example.desporto24.configuration.MediaStorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Component
@ConditionalOnProperty(name = "desporto.media.provider", havingValue = "s3")
public class S3MediaStorage implements MediaStorage {

    private final MediaStorageProperties properties;
    private final S3Client s3Client;

    public S3MediaStorage(MediaStorageProperties properties) {
        this.properties = properties;
        this.s3Client = buildClient(properties);
    }

    @Override
    public URI store(String key, MultipartFile file) {
        MediaStorageSupport.ValidatedMedia media = MediaStorageSupport.validate(file);
        String objectKey = key + media.extension();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(required(properties.getS3Bucket(), "MEDIA_S3_BUCKET"))
                        .key(objectKey)
                        .contentType(media.mimeType())
                        .build(),
                RequestBody.fromBytes(media.bytes())
        );

        String publicBaseUrl = properties.getPublicBaseUrl();
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return URI.create(stripTrailingSlash(publicBaseUrl) + "/" + objectKey);
        }

        return URI.create("s3://" + properties.getS3Bucket() + "/" + objectKey);
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(required(properties.getS3Bucket(), "MEDIA_S3_BUCKET"))
                .key(key)
                .build());
    }

    private static S3Client buildClient(MediaStorageProperties properties) {
        S3Client.Builder builder = S3Client.builder()
                .region(Region.of(properties.getS3Region()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(properties.getS3Endpoint() != null
                                && !properties.getS3Endpoint().isBlank())
                        .build());

        if (properties.getS3Endpoint() != null && !properties.getS3Endpoint().isBlank()) {
            builder.endpointOverride(URI.create(properties.getS3Endpoint()));
        }

        if (properties.getS3AccessKey() != null && !properties.getS3AccessKey().isBlank()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            properties.getS3AccessKey(),
                            required(properties.getS3SecretKey(), "MEDIA_S3_SECRET_KEY")
                    )
            ));
        }

        return builder.build();
    }

    private static String required(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " is required for S3 media storage");
        }
        return value;
    }

    private static String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
