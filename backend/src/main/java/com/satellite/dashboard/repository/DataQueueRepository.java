
package com.satellite.dashboard.repository;

import com.satellite.dashboard.model.DataQueueItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DataQueueRepository
        extends MongoRepository<DataQueueItem, String> {

    List<DataQueueItem> findAllByOrderByPriorityScoreDesc();
}
