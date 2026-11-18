
package com.satellite.dashboard.repository;

import com.satellite.dashboard.model.Telemetry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TelemetryRepository
        extends MongoRepository<Telemetry, String> {

    List<Telemetry> findBySatelliteId(String satelliteId);
}
