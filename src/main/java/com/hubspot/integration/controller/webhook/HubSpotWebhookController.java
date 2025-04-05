package com.hubspot.integration.controller.webhook;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/webhooks")
public class HubSpotWebhookController {
       
    @Value("${hubspot.client.secret}")
    private String clientSecret;
    
    @PostMapping("/contact-creation")
    public ResponseEntity<String> handleContactCreation(
            @RequestBody String payload,
            @RequestHeader("X-HubSpot-Signature") String signature,
            @RequestHeader(value = "X-HubSpot-Signature-Version", required = false) String signatureVersion) {
        
        // 1. Verificar a versão da assinatura
        if (!"v1".equals(signatureVersion)) {
            System.out.println("Versão de assinatura não suportada: " + signatureVersion);
            return ResponseEntity.status(401).body("Versão de assinatura não suportada");
        }
        
        // 2. Validar a assinatura
        if (!isValidSignature(payload, signature)) {
            System.out.println("Assinatura inválida!");
            return ResponseEntity.status(401).body("Assinatura inválida");
        }
        
        // 3. Processar o webhook
        System.out.println("Webhook válido recebido: " + payload);
        return ResponseEntity.ok("Webhook processado com sucesso");
    }
    
    private boolean isValidSignature(String payload, String receivedSignature) {
        try {
            // 1. Concatenar Client Secret + payload (exatamente como recebido)
            String data = this.clientSecret + payload;
            
            // 2. Calcular HMAC-SHA256
            String computedSignature = computeHmacSha256(data);
            
            // 3. Comparar de forma segura contra timing attacks
            return MessageDigest.isEqual(
                computedSignature.getBytes(StandardCharsets.UTF_8),
                receivedSignature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String computeHmacSha256(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        
        // Converter bytes para hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}