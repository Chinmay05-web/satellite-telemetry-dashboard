
package com.satellite.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

@RestController
public class DatabaseController {

    private final MongoTemplate mongoTemplate;

    public DatabaseController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/api/health/database")
    public Map<String, String> checkDatabase() {

        mongoTemplate.getCollectionNames();

        return Map.of(
            "database", "satellite_telemetry_db",
            "status", "Connected",
            "message", "MongoDB connection successful!"
        );
    }
}
