
package com.satellite.dashboard.service;

import com.satellite.dashboard.model.DataQueueItem;
import org.springframework.stereotype.Service;

@Service
public class PriorityService {

    public void calculatePriority(DataQueueItem item) {

        // 1. Calculate urgency score
        double urgencyScore = switch (
                item.getUrgency().toUpperCase()) {

            case "CRITICAL" -> 100;
            case "HIGH" -> 75;
            case "MEDIUM" -> 50;
            case "LOW" -> 25;
            default -> throw new IllegalArgumentException(
                    "Invalid urgency value");
        };

        // 2. Calculate data type score
        double dataScore = switch (
                item.getDataType().toUpperCase()) {

            case "EMERGENCY" -> 100;
            case "SENSOR" -> 80;
            case "IMAGE" -> 60;
            case "HOUSEKEEPING" -> 30;
            default -> throw new IllegalArgumentException(
                    "Invalid data type");
        };

        // 3. Read battery and link quality
        double batteryScore = item.getBatteryLevel();
        double linkScore = item.getLinkQuality();

        // 4. Calculate weighted priority score
        double score =
                (urgencyScore * 0.40) +
                (dataScore * 0.30) +
                (batteryScore * 0.15) +
                (linkScore * 0.15);

        item.setPriorityScore(
                Math.round(score * 100.0) / 100.0
        );

        // 5. Safety-based HOLD recommendation
        if (item.getBatteryLevel() < 15 ||
                item.getLinkQuality() < 20) {

            item.setPriority("HOLD");
            item.setTransmissionStatus("ON_HOLD");
            return;
        }

        // 6. Assign priority
        if (score >= 80) {
            item.setPriority("CRITICAL");

        } else if (score >= 60) {
            item.setPriority("HIGH");

        } else if (score >= 40) {
            item.setPriority("MEDIUM");

        } else {
            item.setPriority("LOW");
        }

        item.setTransmissionStatus("QUEUED");
    }
}
