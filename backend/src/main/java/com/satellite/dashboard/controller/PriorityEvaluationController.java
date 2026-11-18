
package com.satellite.dashboard.controller;

import com.satellite.dashboard.model.PriorityEvaluation;
import com.satellite.dashboard.service.PriorityEvaluationService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/priority")
@CrossOrigin(origins = "http://localhost:5173")
public class PriorityEvaluationController {

    private final PriorityEvaluationService evaluationService;

    public PriorityEvaluationController(
            PriorityEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    // Evaluate satellite data and save the result
    @PostMapping("/evaluate")
    @ResponseStatus(HttpStatus.CREATED)
    public PriorityEvaluation evaluate(
            @Valid @RequestBody PriorityEvaluation evaluation) {

        return evaluationService.evaluate(evaluation);
    }

    // Retrieve previous evaluation results
    @GetMapping("/history")
    public List<PriorityEvaluation> getEvaluationHistory() {

        return evaluationService.getEvaluationHistory();
    }
}
