package com.hubspot.integration.dto.webhook;

import lombok.Data;

@Data
public class WebhookResponse {
    private String status;
    private String message;
    
    public WebhookResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public static WebhookResponse success() {
        return new WebhookResponse("success", "Webhook processed successfully");
    }
    
    public static WebhookResponse error(String message) {
        return new WebhookResponse("error", message);
    }
}