package com.hubspot.integration.service.webhook;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class HubSpotWebhookService {

    public void processContactCreation(String payload) {
        log.info("Webhook válido recebido: {}", payload);
    }
}
