package com.example.sensor_service.service.impl;

import com.example.sensor_service.dto.*;
import com.example.sensor_service.exception.ExternalApiException;
import com.example.sensor_service.service.ExternalIoTService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalIoTServiceImpl implements ExternalIoTService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalIoTServiceImpl.class);

    private final WebClient webClient;

    @Value("${iot.api.username}")
    private String username;

    @Value("${iot.api.password}")
    private String password;

    private String accessToken;
    private String refreshToken;

    public ExternalIoTServiceImpl(@Value("${iot.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public void login() {
        logger.info("Logging in to external IoT API with username: {}", username);
        try {
            AuthRequestDTO authRequest = new AuthRequestDTO(username, password);
            AuthResponseDTO authResponse = webClient.post()
                .uri("/auth/login")
                .bodyValue(authRequest)
                .retrieve()
                .bodyToMono(AuthResponseDTO.class)
                .block();

            if (authResponse != null) {
                this.accessToken = authResponse.getAccessToken();
                this.refreshToken = authResponse.getRefreshToken();
                logger.info("Login successful. Access token obtained.");
            }
        } catch (Exception e) {
            logger.error("Failed to login to external IoT API", e);
            throw new ExternalApiException("Failed to login to external IoT API: " + e.getMessage());
        }
    }

    @Override
    public void refreshAccessToken() {
        logger.info("Refreshing access token...");
        try {
            var body = java.util.Map.of("refreshToken", this.refreshToken);
            AuthResponseDTO authResponse = webClient.post()
                .uri("/auth/refresh")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AuthResponseDTO.class)
                .block();

            if (authResponse != null) {
                this.accessToken = authResponse.getAccessToken();
                this.refreshToken = authResponse.getRefreshToken();
                logger.info("Token refreshed successfully.");
            }
        } catch (Exception e) {
            logger.warn("Token refresh failed. Re-logging in...", e);
            login();
        }
    }

    @Override
    public DeviceResponseDTO registerDevice(DeviceRequestDTO request) {
        ensureAuthenticated();
        logger.info("Registering device: {} for zone: {}", request.getName(), request.getZoneId());
        try {
            return webClient.post()
                .uri("/devices")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeviceResponseDTO.class)
                .block();
        } catch (Exception e) {
            logger.error("Failed to register device", e);
            refreshAccessToken();
            return webClient.post()
                .uri("/devices")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeviceResponseDTO.class)
                .block();
        }
    }

    @Override
    public DeviceResponseDTO[] getAllDevices() {
        ensureAuthenticated();
        logger.info("Fetching all devices from external IoT API");
        return webClient.get()
            .uri("/devices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(DeviceResponseDTO[].class)
            .block();
    }

    @Override
    public TelemetryResponseDTO getDeviceTelemetry(String deviceId) {
        ensureAuthenticated();
        logger.info("Fetching telemetry for device: {}", deviceId);
        try {
            return webClient.get()
                .uri("/devices/telemetry/{deviceId}", deviceId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(TelemetryResponseDTO.class)
                .block();
        } catch (Exception e) {
            logger.error("Failed to fetch telemetry for device: {}", deviceId, e);
            refreshAccessToken();
            return webClient.get()
                .uri("/devices/telemetry/{deviceId}", deviceId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(TelemetryResponseDTO.class)
                .block();
        }
    }

    private void ensureAuthenticated() {
        if (this.accessToken == null) {
            login();
        }
    }
}
