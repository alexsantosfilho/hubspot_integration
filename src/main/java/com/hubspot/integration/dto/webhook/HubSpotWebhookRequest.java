package com.hubspot.integration.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HubSpotWebhookRequest {
    @JsonProperty("subscriptionId")
    private Long subscriptionId;
    
    @JsonProperty("portalId")
    private Long portalId;
    
    @JsonProperty("appId")
    private Long appId;
    
    @JsonProperty("occurredAt")
    private Long occurredAt;
    
    @JsonProperty("attemptNumber")
    private Integer attemptNumber;
    
    @JsonProperty("eventId")
    private Long eventId;

    @JsonProperty("sourceId")
    private String sourceId;
    
    @JsonProperty("events")
    private List<HubSpotWebhookEvent> events;
}