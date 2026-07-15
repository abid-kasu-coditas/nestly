package com.nestly.shared.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class S3PresignHelperTest {

    private final S3PresignHelper helper =
            new S3PresignHelper("ap-south-1", "test", "test", "nestly-docs", null);

    @Test
    void presignPutBuildsKeyAndSignedUrl() {
        S3PresignHelper.PresignedUpload upload = helper.presignPut("listings/abc", "photo.png");

        assertThat(upload.key()).startsWith("listings/abc/").endsWith("-photo.png");
        assertThat(upload.url()).contains("nestly-docs").contains("X-Amz-Signature");
    }

    @Test
    void presignGetSignsTheKey() {
        String url = helper.presignGet("listings/abc/some-file.pdf");

        assertThat(url).contains("listings/abc/some-file.pdf").contains("X-Amz-Signature");
    }
}
