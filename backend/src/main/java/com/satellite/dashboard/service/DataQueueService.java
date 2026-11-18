
package com.satellite.dashboard.service;

import com.satellite.dashboard.model.DataQueueItem;
import com.satellite.dashboard.repository.DataQueueRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataQueueService {

    private final DataQueueRepository repository;
    private final PriorityService priorityService;

    public DataQueueService(
            DataQueueRepository repository,
            PriorityService priorityService) {

        this.repository = repository;
        this.priorityService = priorityService;
    }

    public DataQueueItem addQueueItem(DataQueueItem item) {

        item.setCreatedAt(LocalDateTime.now());

        priorityService.calculatePriority(item);

        return repository.save(item);
    }

    public List<DataQueueItem> getAllQueueItems() {
        return repository.findAllByOrderByPriorityScoreDesc();
    }

    public DataQueueItem getQueueItemById(String id) {
        return repository.findById(id).orElse(null);
    }
}
