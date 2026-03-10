package com.example.sensor_service.feign;

import com.example.sensor_service.dto.TelemetryResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "AUTOMATION-SERVICE")
public interface AutomationClient {

    @PostMapping("/api/automation/process")
    Map<String, Object> processTelemetry(@RequestBody TelemetryResponseDTO telemetry);
}
