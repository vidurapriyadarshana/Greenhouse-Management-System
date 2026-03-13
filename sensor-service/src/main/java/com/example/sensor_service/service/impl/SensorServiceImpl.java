package com.example.sensor_service.service.impl;

import com.example.sensor_service.dto.DeviceRequestDTO;
import com.example.sensor_service.dto.DeviceResponseDTO;
import com.example.sensor_service.dto.TelemetryResponseDTO;
import com.example.sensor_service.service.ExternalIoTService;
import com.example.sensor_service.service.SensorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SensorServiceImpl implements SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorServiceImpl.class);

    private final ExternalIoTService externalIoTService;

    private final ConcurrentHashMap<String, TelemetryResponseDTO> latestReadings =
        new ConcurrentHashMap<>();

    public SensorServiceImpl(ExternalIoTService externalIoTService) {
        this.externalIoTService = externalIoTService;
    }

    @Override
    public DeviceResponseDTO registerDevice(DeviceRequestDTO request) {
        logger.info("Registering device via external IoT API: {}", request.getName());
        return externalIoTService.registerDevice(request);
    }

    @Override
    public void storeLatestReading(TelemetryResponseDTO telemetry) {
        latestReadings.put(telemetry.getDeviceId(), telemetry);
    }

    @Override
    public Map<String, TelemetryResponseDTO> getLatestReadings() {
        return latestReadings;
    }
}
