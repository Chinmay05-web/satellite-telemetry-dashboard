
package com.satellite.dashboard.controller;

import com.satellite.dashboard.model.DataQueueItem;
import com.satellite.dashboard.service.DataQueueService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "http://localhost:5173")
public class DataQueueController {

    private final DataQueueService service;

    public DataQueueController(DataQueueService service) {
        this.service = service;
    }

    // ADD DATA AND CALCULATE PRIORITY
    @PostMapping
    public ResponseEntity<DataQueueItem> addItem(
            @Valid @RequestBody DataQueueItem item) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addQueueItem(item));
    }

    // GET ALL QUEUED DATA
    @GetMapping
    public List<DataQueueItem> getAllItems() {
        return service.getAllQueueItems();
    }

    // GET PARTICULAR QUEUE ITEM
    @GetMapping("/{id}")
    public ResponseEntity<DataQueueItem> getItem(
            @PathVariable String id) {

        DataQueueItem item = service.getQueueItemById(id);

        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item);
    }
}
