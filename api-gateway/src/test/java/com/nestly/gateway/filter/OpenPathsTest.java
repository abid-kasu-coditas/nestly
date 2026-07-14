package com.nestly.gateway.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OpenPathsTest {

    @Test
    void otpEndpointsAreOpen() {
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/auth/request-otp")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/auth/verify-otp")).isTrue();
    }

    @Test
    void publicListingBrowseIsOpen() {
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/listings")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("GET",
                "/listings/7b0f7d64-6f2e-4f7b-9a5b-0f13e7cbe1aa")).isTrue();
    }

    @Test
    void swaggerAssetsAreOpen() {
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/swagger-ui.html")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/swagger-ui/index.html")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/webjars/swagger-ui/swagger-ui.css")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/docs/auth/v3/api-docs")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/docs/listing/v3/api-docs")).isTrue();
    }

    @Test
    void internalServiceCallsAreOpen() {
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/internal/notifications/notify")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/internal/notifications/otp-email")).isTrue();
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/listings/internal/verification-result")).isFalse();
    }

    @Test
    void everythingElseNeedsAuth() {
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/listings/my")).isFalse();
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/listings")).isFalse();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/notifications/my")).isFalse();
        assertThat(JwtAuthGlobalFilter.isOpen("POST", "/auth/admin/create-account")).isFalse();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/applications/my")).isFalse();
        assertThat(JwtAuthGlobalFilter.isOpen("GET", "/verification/results")).isFalse();
    }
}
