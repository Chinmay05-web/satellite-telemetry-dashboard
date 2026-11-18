
package com.satellite.dashboard.controller;

import com.satellite.dashboard.model.Telemetry;
import com.satellite.dashboard.service.TelemetryService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/telemetry")
@CrossOrigin(origins = "http://localhost:5173")
public class TelemetryController {

    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Telemetry> addTelemetry(
            @Valid @RequestBody Telemetry telemetry) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addTelemetry(telemetry));
    }

    // READ ALL
    @GetMapping
    public List<Telemetry> getAllTelemetry() {
        return service.getAllTelemetry();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Telemetry> getTelemetryById(
            @PathVariable String id) {

        return service.getTelemetryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ BY SATELLITE ID
    @GetMapping("/satellite/{satelliteId}")
    public List<Telemetry> getBySatelliteId(
            @PathVariable String satelliteId) {

        return service.getBySatelliteId(satelliteId);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Telemetry> updateTelemetry(
            @PathVariable String id,
            @Valid @RequestBody Telemetry telemetry) {

        return service.updateTelemetry(id, telemetry)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTelemetry(
            @PathVariable String id) {

        if (service.getTelemetryById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteTelemetry(id);

        return ResponseEntity.noContent().build();
    }
}
