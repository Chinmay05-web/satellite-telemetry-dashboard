
package com.satellite.dashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transmission_history")
public class TransmissionHistory {

    @Id
    private String id;

    private String queueItemId;
    private String satelliteId;
    private String dataType;
    private String priority;
    private double priorityScore;
    private String transmissionStatus;
    private LocalDateTime transmissionTime;
    private String decision;

    public TransmissionHistory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueueItemId() {
        return queueItemId;
    }

    public void setQueueItemId(String queueItemId) {
        this.queueItemId = queueItemId;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public String getTransmissionStatus() {
        return transmissionStatus;
    }

    public void setTransmissionStatus(String transmissionStatus) {
        this.transmissionStatus = transmissionStatus;
    }

    public LocalDateTime getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(LocalDateTime transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
