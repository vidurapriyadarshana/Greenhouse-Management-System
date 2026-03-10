package com.example.automation_service.controller;

import com.example.automation_service.common.CommonResponse;
import com.example.automation_service.dto.ActionLogResponseDTO;
import com.example.automation_service.dto.TelemetryInputDTO;
import com.example.automation_service.service.AutomationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    private final AutomationService automationService;

    public AutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }

    @PostMapping("/process")
    public ResponseEntity<CommonResponse<String>> processTelemetry(
            @RequestBody TelemetryInputDTO telemetry) {
        automationService.processTelemetry(telemetry);
        CommonResponse<String> response = new CommonResponse<>(
            200, "OK", "Telemetry processed successfully", null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logs")
    public ResponseEntity<CommonResponse<List<ActionLogResponseDTO>>> getAllActionLogs() {
        List<ActionLogResponseDTO> logs = automationService.getAllActionLogs();
        CommonResponse<List<ActionLogResponseDTO>> response = new CommonResponse<>(
            200, "OK", "Action logs fetched successfully", logs
        );
        return ResponseEntity.ok(response);
    }
}
