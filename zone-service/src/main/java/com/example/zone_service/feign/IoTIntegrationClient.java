package com.example.zone_service.feign;

import com.example.zone_service.dto.DeviceRegistrationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "SENSOR-SERVICE")
public interface IoTIntegrationClient {

    @PostMapping("/api/sensors/devices")
    Map<String, Object> registerDevice(@RequestBody DeviceRegistrationDTO dto);
}
