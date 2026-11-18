
package com.satellite.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> health() {

        return Map.of(
            "status", "Running",
            "application", "Satellite Telemetry Dashboard",
            "message", "Backend is working successfully!"
        );
    }
}
