package com.hubspot.integration.controller.webhook;

import org.springframework.web.bind.annotation.*;

import com.hubspot.integration.service.webhook.HubSpotWebhookService;
import com.hubspot.integration.service.webhook.SignatureService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/webhooks")
public class HubSpotWebhookController {
    private final SignatureService signatureService;
    private final HubSpotWebhookService webhookService;

    public HubSpotWebhookController(SignatureService signatureService, 
                                  HubSpotWebhookService webhookService) {
        this.signatureService = signatureService;
        this.webhookService = webhookService;
    }

    @PostMapping("/contact-creation")
    public ResponseEntity<String> handleContactCreation(
            @RequestBody String payload,
            @RequestHeader("X-HubSpot-Signature") String signature,
            @RequestHeader(value = "X-HubSpot-Signature-Version", required = false) String signatureVersion) {
        
        if (!signatureService.isValidSignature(payload, signature, signatureVersion)) {
            return ResponseEntity.status(401).body("Assinatura inválida ou versão não suportada");
        }
        
        webhookService.processContactCreation(payload);
        return ResponseEntity.ok("Webhook processado com sucesso");
    }
}