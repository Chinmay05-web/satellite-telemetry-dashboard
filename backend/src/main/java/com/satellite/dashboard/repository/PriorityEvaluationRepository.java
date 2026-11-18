
package com.satellite.dashboard.repository;

import com.satellite.dashboard.model.PriorityEvaluation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PriorityEvaluationRepository
        extends MongoRepository<PriorityEvaluation, String> {

    List<PriorityEvaluation> findAllByOrderByCreatedAtDesc();
}
