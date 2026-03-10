package com.example.sensor_service.service;

import com.example.sensor_service.dto.DeviceResponseDTO;
import com.example.sensor_service.dto.TelemetryResponseDTO;
import com.example.sensor_service.feign.AutomationClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TelemetryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TelemetryScheduler.class);

    private final ExternalIoTService externalIoTService;
    private final AutomationClient automationClient;
    private final SensorService sensorService;

    public TelemetryScheduler(ExternalIoTService externalIoTService,
                               AutomationClient automationClient,
                               SensorService sensorService) {
        this.externalIoTService = externalIoTService;
        this.automationClient = automationClient;
        this.sensorService = sensorService;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndPushTelemetry() {
        logger.info("Scheduled task: Fetching telemetry data...");
        try {
            DeviceResponseDTO[] devices = externalIoTService.getAllDevices();
            if (devices == null || devices.length == 0) {
                logger.warn("No devices found. Skipping telemetry fetch.");
                return;
            }

            for (DeviceResponseDTO device : devices) {
                try {
                    TelemetryResponseDTO telemetry =
                        externalIoTService.getDeviceTelemetry(device.getDeviceId());

                    sensorService.storeLatestReading(telemetry);

                    automationClient.processTelemetry(telemetry);

                    logger.info("Telemetry pushed for device: {} | Temp: {} | Humidity: {}",
                        device.getDeviceId(),
                        telemetry.getValue().getTemperature(),
                        telemetry.getValue().getHumidity());
                } catch (Exception e) {
                    logger.error("Error processing telemetry for device: {}",
                        device.getDeviceId(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Scheduled telemetry fetch failed", e);
        }
    }
}
