
package com.satellite.dashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Document(collection = "data_queue")
public class DataQueueItem {

    @Id
    private String id;

    @NotBlank
    private String satelliteId;

    @NotBlank
    private String dataType;

    @NotBlank
    private String urgency;

    @Min(0)
    @Max(100)
    private int batteryLevel;

    @Min(0)
    @Max(100)
    private int linkQuality;

    private double priorityScore;

    private String priority;

    private String transmissionStatus;

    private LocalDateTime createdAt;

    public DataQueueItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getLinkQuality() {
        return linkQuality;
    }

    public void setLinkQuality(int linkQuality) {
        this.linkQuality = linkQuality;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTransmissionStatus() {
        return transmissionStatus;
    }

    public void setTransmissionStatus(String transmissionStatus) {
        this.transmissionStatus = transmissionStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
