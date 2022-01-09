package com.siemplify.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dto object for returning task error percentage as json
 */
public class TaskErrorPerc {

    private final static String ERROR_PERCENTAGE = "error_percentage";
    private final static String CONSUMER_ID = "consumer_id";

    @JsonProperty(CONSUMER_ID)
    private String consumerId;

    @JsonProperty(ERROR_PERCENTAGE)
    private Double errorPercentage;

    public TaskErrorPerc() {}

    public TaskErrorPerc(String consumerId, Double errorPercentage) {
        this.consumerId = consumerId;
        this.errorPercentage = errorPercentage;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Double getErrorPercentage() {
        return errorPercentage;
    }

    public void setErrorPercentage(Double errorPercentage) {
        this.errorPercentage = errorPercentage;
    }
}
