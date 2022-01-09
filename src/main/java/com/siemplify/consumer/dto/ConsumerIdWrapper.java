package com.siemplify.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Dto object wrapper for passing consumer Ids as a list
 */
public class ConsumerIdWrapper {

    private final static String CONSUMER_IDS = "consumer_ids";

    @JsonProperty(CONSUMER_IDS)
    private List<String> consumerIds;

    public List<String> getConsumerIds() {
        return consumerIds;
    }

    public void setConsumerIds(List<String> consumerIds) {
        this.consumerIds = consumerIds;
    }
}
