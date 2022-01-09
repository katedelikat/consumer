package com.siemplify.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dto object for returning average task execution in json
 */
public class TaskExecutionTime {

    private final static String AVG_EXEC_TIME = "avg_exec_time";
    private final static String CONSUMER_ID = "consumer_id";

    @JsonProperty(CONSUMER_ID)
    private String consumerId;

    @JsonProperty(AVG_EXEC_TIME)
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
