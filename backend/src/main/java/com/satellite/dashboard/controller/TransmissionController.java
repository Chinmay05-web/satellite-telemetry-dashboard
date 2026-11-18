
package com.satellite.dashboard.controller;

import com.satellite.dashboard.model.TransmissionHistory;
import com.satellite.dashboard.service.TransmissionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/transmission")
@CrossOrigin(origins = "http://localhost:5173")
public class TransmissionController {

    private final TransmissionService transmissionService;

    public TransmissionController(
            TransmissionService transmissionService) {
        this.transmissionService = transmissionService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> transmit(@PathVariable String id) {
        try {
            TransmissionHistory history =
                    transmissionService.transmit(id);

            return ResponseEntity.ok(history);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        } catch (RuntimeException e) {
            if ("Queue item not found".equals(e.getMessage())) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.internalServerError()
                    .body("Transmission failed");
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransmissionHistory>> getHistory() {
        return ResponseEntity.ok(
                transmissionService.getAllHistory());
    }
}
