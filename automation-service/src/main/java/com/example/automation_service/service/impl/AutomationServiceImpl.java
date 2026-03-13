package com.example.automation_service.service.impl;

import com.example.automation_service.dto.ActionLogResponseDTO;
import com.example.automation_service.dto.TelemetryInputDTO;
import com.example.automation_service.dto.ZoneThresholdDTO;
import com.example.automation_service.exception.ZoneThresholdFetchException;
import com.example.automation_service.feign.ZoneServiceClient;
import com.example.automation_service.model.ActionLog;
import com.example.automation_service.repository.ActionLogRepository;
import com.example.automation_service.service.AutomationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AutomationServiceImpl implements AutomationService {

    private static final Logger logger = LoggerFactory.getLogger(AutomationServiceImpl.class);

    private final ActionLogRepository actionLogRepository;
    private final ZoneServiceClient zoneServiceClient;
    private final ObjectMapper objectMapper;

    public AutomationServiceImpl(ActionLogRepository actionLogRepository,
                                  ZoneServiceClient zoneServiceClient,
                                  ObjectMapper objectMapper) {
        this.actionLogRepository = actionLogRepository;
        this.zoneServiceClient = zoneServiceClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processTelemetry(TelemetryInputDTO telemetry) {
        logger.info("Processing telemetry for device: {} in zone: {}",
            telemetry.getDeviceId(), telemetry.getZoneId());

        ZoneThresholdDTO zoneThreshold;
        try {
            Map<String, Object> response = zoneServiceClient.getZoneById(telemetry.getZoneId());
            Object data = response.get("data");
            zoneThreshold = objectMapper.convertValue(data, ZoneThresholdDTO.class);
        } catch (Exception e) {
            logger.error("Failed to fetch zone thresholds for zone: {}", telemetry.getZoneId(), e);
            throw new ZoneThresholdFetchException(
                "Could not fetch thresholds for zone: " + telemetry.getZoneId());
        }

        Double currentTemp = telemetry.getValue().getTemperature();
        Double minTemp = zoneThreshold.getMinTemp();
        Double maxTemp = zoneThreshold.getMaxTemp();

        logger.info("Zone: {} | Current Temp: {} | Min: {} | Max: {}",
            telemetry.getZoneId(), currentTemp, minTemp, maxTemp);

        if (currentTemp > maxTemp) {
            logAction(telemetry, zoneThreshold, "TURN_FAN_ON");
            logger.warn("ACTION: TURN_FAN_ON triggered for zone {} | Temp {} > Max {}",
                telemetry.getZoneId(), currentTemp, maxTemp);
        } else if (currentTemp < minTemp) {
            logAction(telemetry, zoneThreshold, "TURN_HEATER_ON");
            logger.warn("ACTION: TURN_HEATER_ON triggered for zone {} | Temp {} < Min {}",
                telemetry.getZoneId(), currentTemp, minTemp);
        } else {
            logger.info("Zone {} is within normal range. No action needed.", telemetry.getZoneId());
        }
    }

    private void logAction(TelemetryInputDTO telemetry, ZoneThresholdDTO zone, String action) {
        ActionLog log = new ActionLog();
        log.setZoneId(telemetry.getZoneId());
        log.setDeviceId(telemetry.getDeviceId());
        log.setAction(action);
        log.setCurrentTemp(telemetry.getValue().getTemperature());
        log.setMinTemp(zone.getMinTemp());
        log.setMaxTemp(zone.getMaxTemp());
        actionLogRepository.save(log);
    }

    @Override
    public List<ActionLogResponseDTO> getAllActionLogs() {
        logger.info("Fetching all action logs");
        return actionLogRepository.findAllByOrderByTriggeredAtDesc().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private ActionLogResponseDTO mapToDTO(ActionLog log) {
        ActionLogResponseDTO dto = new ActionLogResponseDTO();
        dto.setId(log.getId());
        dto.setZoneId(log.getZoneId());
        dto.setDeviceId(log.getDeviceId());
        dto.setAction(log.getAction());
        dto.setCurrentTemp(log.getCurrentTemp());
        dto.setMinTemp(log.getMinTemp());
        dto.setMaxTemp(log.getMaxTemp());
        dto.setTriggeredAt(log.getTriggeredAt());
        return dto;
    }
}
