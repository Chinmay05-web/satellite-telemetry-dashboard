
package com.satellite.dashboard.repository;

import com.satellite.dashboard.model.TransmissionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransmissionHistoryRepository
        extends MongoRepository<TransmissionHistory, String> {

    List<TransmissionHistory>
        findAllByOrderByTransmissionTimeDesc();
}
