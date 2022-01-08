package com.siemplify.consumer.dto;

public class TaskErrorPerc {

    private String consumerId;
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
