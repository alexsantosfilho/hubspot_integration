package com.hubspot.integration.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class HubSpotWebhookPayload {
    @JsonProperty("objectId")
    private Long objectId;
    
    @JsonProperty("propertyName")
    private String propertyName;
    
    @JsonProperty("propertyValue")
    private String propertyValue;
    
    @JsonProperty("changeSource")
    private String changeSource;
    
    @JsonProperty("changeFlag")
    private String changeFlag;
    
    @JsonProperty("subscriptionId")
    private Long subscriptionId;
    
    @JsonProperty("subscriptionType")
    private String subscriptionType;
    
    @JsonProperty("portalId")
    private Long portalId;
    
    @JsonProperty("appId")
    private Long appId;
    
    @JsonProperty("occurredAt")
    private Long occurredAt;
    
    @JsonProperty("properties")
    private Map<String, String> properties;
}