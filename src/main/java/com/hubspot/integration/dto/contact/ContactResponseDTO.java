package com.hubspot.integration.dto.contact;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponseDTO {

    private boolean success;
    private String message;

    @JsonProperty("id")
    private String id;
    
    @JsonProperty("properties")
    private Map<String, String> properties;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("updatedAt")
    private String updatedAt;
    
    @JsonProperty("archived")
    private boolean archived;

}