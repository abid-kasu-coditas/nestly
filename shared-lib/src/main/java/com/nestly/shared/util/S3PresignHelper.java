package com.nestly.shared.util;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

public class S3PresignHelper {

    private static final Duration PUT_EXPIRY = Duration.ofMinutes(10);
    private static final Duration GET_EXPIRY = Duration.ofHours(1);

    private final S3Presigner presigner;
    private final String bucket;

    public S3PresignHelper(String region, String accessKey, String secretKey,
                           String bucket, String endpointOverride) {
        S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)));
        if (endpointOverride != null && !endpointOverride.isBlank()) {
            builder.endpointOverride(URI.create(endpointOverride));
        }
        this.presigner = builder.build();
        this.bucket = bucket;
    }

    public PresignedUpload presignPut(String prefix, String filename) {
        String key = prefix + "/" + UUID.randomUUID() + "-" + filename;
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(PUT_EXPIRY)
                .putObjectRequest(putRequest)
                .build();
        String url = presigner.presignPutObject(presignRequest).url().toString();
        return new PresignedUpload(key, url);
    }

    public String presignGet(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(GET_EXPIRY)
                .getObjectRequest(getRequest)
                .build();
        return presigner.presignGetObject(presignRequest).url().toString();
    }

    public record PresignedUpload(String key, String url) {
    }
}
