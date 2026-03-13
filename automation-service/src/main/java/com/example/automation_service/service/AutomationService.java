package com.example.automation_service.service;

import com.example.automation_service.dto.ActionLogResponseDTO;
import com.example.automation_service.dto.TelemetryInputDTO;

import java.util.List;

public interface AutomationService {

    void processTelemetry(TelemetryInputDTO telemetry);

    List<ActionLogResponseDTO> getAllActionLogs();
}
