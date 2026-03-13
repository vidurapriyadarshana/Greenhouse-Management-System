package com.example.sensor_service.service;

import com.example.sensor_service.dto.DeviceRequestDTO;
import com.example.sensor_service.dto.DeviceResponseDTO;
import com.example.sensor_service.dto.TelemetryResponseDTO;

public interface ExternalIoTService {

    void login();

    void refreshAccessToken();

    DeviceResponseDTO registerDevice(DeviceRequestDTO request);

    DeviceResponseDTO[] getAllDevices();

    TelemetryResponseDTO getDeviceTelemetry(String deviceId);
}
