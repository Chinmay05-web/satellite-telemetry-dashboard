
package com.satellite.dashboard.service;

import com.satellite.dashboard.model.Telemetry;
import com.satellite.dashboard.repository.TelemetryRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TelemetryService {

    private final TelemetryRepository repository;

    public TelemetryService(TelemetryRepository repository) {
        this.repository = repository;
    }

    public Telemetry addTelemetry(Telemetry telemetry) {

        telemetry.setTimestamp(LocalDateTime.now());
        telemetry.setStatus("ACTIVE");

        return repository.save(telemetry);
    }

    public List<Telemetry> getAllTelemetry() {
        return repository.findAll();
    }

    public Optional<Telemetry> getTelemetryById(String id) {
        return repository.findById(id);
    }

    public List<Telemetry> getBySatelliteId(String satelliteId) {
        return repository.findBySatelliteId(satelliteId);
    }

    public Optional<Telemetry> updateTelemetry(
            String id, Telemetry updatedTelemetry) {

        return repository.findById(id).map(existing -> {

            existing.setSatelliteId(
                updatedTelemetry.getSatelliteId()
            );

            existing.setBatteryLevel(
                updatedTelemetry.getBatteryLevel()
            );

            existing.setLinkQuality(
                updatedTelemetry.getLinkQuality()
            );

            existing.setDataType(
                updatedTelemetry.getDataType()
            );

            existing.setUrgency(
                updatedTelemetry.getUrgency()
            );

            return repository.save(existing);
        });
    }

    public void deleteTelemetry(String id) {
        repository.deleteById(id);
    }
}
