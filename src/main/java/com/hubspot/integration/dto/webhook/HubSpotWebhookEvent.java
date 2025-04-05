package com.hubspot.integration.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class HubSpotWebhookEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("subscriptionType")
    private String subscriptionType;

    @JsonProperty("objectId")
    private Long objectId;

    @JsonProperty("properties")
    private Map<String, String> properties;

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

    @JsonProperty("portalId")
    private Long portalId;

    @JsonProperty("appId")
    private Long appId;

    @JsonProperty("occurredAt")
    private Long occurredAt;

    @JsonProperty("eventId")
    private Long eventId;

    @JsonProperty("attemptNumber")
    private Integer attemptNumber;

    @JsonProperty("sourceId")
    private String sourceId;
}
