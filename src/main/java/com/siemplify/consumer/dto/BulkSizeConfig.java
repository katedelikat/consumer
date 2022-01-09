package com.siemplify.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dto object for passing bulk size configuration
 */
public class BulkSizeConfig {

    private final static String BULK_SIZE = "bulk_size";
    private final static String CONSUMER_ID = "consumer_id";

    @JsonProperty(CONSUMER_ID)
    private String consumerId;

    @JsonProperty(BULK_SIZE)
    private Integer bulkSize;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getBulkSize() {
        return bulkSize;
    }

    public void setBulkSize(Integer bulkSize) {
        this.bulkSize = bulkSize;
    }
}
