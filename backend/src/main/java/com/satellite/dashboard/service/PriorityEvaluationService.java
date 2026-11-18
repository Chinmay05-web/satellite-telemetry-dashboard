
package com.satellite.dashboard.service;

import com.satellite.dashboard.model.DataQueueItem;
import com.satellite.dashboard.model.PriorityEvaluation;
import com.satellite.dashboard.repository.PriorityEvaluationRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PriorityEvaluationService {

    private final PriorityService priorityService;
    private final PriorityEvaluationRepository evaluationRepository;

    public PriorityEvaluationService(
            PriorityService priorityService,
            PriorityEvaluationRepository evaluationRepository) {

        this.priorityService = priorityService;
        this.evaluationRepository = evaluationRepository;
    }

    public PriorityEvaluation evaluate(PriorityEvaluation evaluation) {

        // Convert evaluation input into the existing queue model
        DataQueueItem item = new DataQueueItem();

        item.setSatelliteId(evaluation.getSatelliteId());
        item.setDataType(evaluation.getDataType());
        item.setUrgency(evaluation.getUrgency());
        item.setBatteryLevel(evaluation.getBatteryLevel());
        item.setLinkQuality(evaluation.getLinkQuality());

        // Reuse the existing intelligent priority algorithm
        priorityService.calculatePriority(item);

        // Copy calculated results
        evaluation.setPriorityScore(item.getPriorityScore());
        evaluation.setPriority(item.getPriority());
        evaluation.setTransmissionStatus(item.getTransmissionStatus());

        // Generate explanation based on actual input and result
        evaluation.setExplanation(generateExplanation(evaluation));

        // Record evaluation time
        evaluation.setCreatedAt(LocalDateTime.now());

        // Save evaluation independently in MongoDB
        return evaluationRepository.save(evaluation);
    }

    private String generateExplanation(PriorityEvaluation evaluation) {

        StringBuilder explanation = new StringBuilder();

        explanation.append("Satellite ")
                .append(evaluation.getSatelliteId())
                .append(" was evaluated. ");

        explanation.append("Urgency: ")
                .append(evaluation.getUrgency())
                .append(", data type: ")
                .append(evaluation.getDataType())
                .append(". ");

        explanation.append("Battery level: ")
                .append(evaluation.getBatteryLevel())
                .append("%, link quality: ")
                .append(evaluation.getLinkQuality())
                .append("%. ");

        if (evaluation.getBatteryLevel() < 15) {
            explanation.append(
                    "HOLD recommended because battery level is below 15%. ");

        } else if (evaluation.getLinkQuality() < 20) {
            explanation.append(
                    "HOLD recommended because link quality is below 20%. ");

        } else {
            explanation.append("Weighted priority score: ")
                    .append(evaluation.getPriorityScore())
                    .append(". ");

            explanation.append("The assigned priority is ")
                    .append(evaluation.getPriority())
                    .append(". ");

            explanation.append(
                    "The item is recommended for the transmission queue.");
        }

        return explanation.toString();
    }

    public java.util.List<PriorityEvaluation> getEvaluationHistory() {
        return evaluationRepository.findAllByOrderByCreatedAtDesc();
    }
}
