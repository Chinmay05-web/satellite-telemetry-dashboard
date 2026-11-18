
package com.satellite.dashboard.service;

import com.satellite.dashboard.model.DataQueueItem;
import com.satellite.dashboard.model.TransmissionHistory;
import com.satellite.dashboard.repository.DataQueueRepository;
import com.satellite.dashboard.repository.TransmissionHistoryRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransmissionService {

    private final DataQueueRepository queueRepository;
    private final TransmissionHistoryRepository historyRepository;

    public TransmissionService(
            DataQueueRepository queueRepository,
            TransmissionHistoryRepository historyRepository) {
        this.queueRepository = queueRepository;
        this.historyRepository = historyRepository;
    }

    public TransmissionHistory transmit(String id) {

        DataQueueItem item = queueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Queue item not found"));

        if ("HOLD".equalsIgnoreCase(item.getPriority())
                || "ON_HOLD".equalsIgnoreCase(
                        item.getTransmissionStatus())) {
            throw new IllegalStateException(
                    "This item is on HOLD and cannot be transmitted");
        }

        if ("TRANSMITTED".equalsIgnoreCase(
                item.getTransmissionStatus())) {
            throw new IllegalStateException(
                    "This item has already been transmitted");
        }

        item.setTransmissionStatus("TRANSMITTED");
        queueRepository.save(item);

        TransmissionHistory history = new TransmissionHistory();

        history.setQueueItemId(item.getId());
        history.setSatelliteId(item.getSatelliteId());
        history.setDataType(item.getDataType());
        history.setPriority(item.getPriority());
        history.setPriorityScore(item.getPriorityScore());
        history.setTransmissionStatus("TRANSMITTED");
        history.setTransmissionTime(LocalDateTime.now());
        history.setDecision("Transmission simulated successfully");

        return historyRepository.save(history);
    }

    public List<TransmissionHistory> getAllHistory() {
        return historyRepository
                .findAllByOrderByTransmissionTimeDesc();
    }
}
