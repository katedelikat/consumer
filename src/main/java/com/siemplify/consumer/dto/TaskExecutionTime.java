package com.siemplify.consumer.dto;

public class TaskExecutionTime {

    private String consumerId;
    private Double avgExecTimeMs;

    public TaskExecutionTime() {}

    public TaskExecutionTime(String consumerId, Double avgExecTime) {
        this.consumerId = consumerId;
        this.avgExecTimeMs = avgExecTime;

    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Double getAvgExecTimeMs() {
        return avgExecTimeMs;
    }

    public void setAvgExecTimeMs(Double avgExecTimeMs) {
        this.avgExecTimeMs = avgExecTimeMs;
    }
}
