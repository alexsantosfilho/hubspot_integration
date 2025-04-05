package com.hubspot.integration.service.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class SignatureService {
    @Value("${hubspot.client.secret}")
    private String clientSecret;

    public boolean isValidSignature(String payload, String receivedSignature, String signatureVersion) {
        if (!"v1".equals(signatureVersion)) {
            return false;
        }
        
        String data = this.clientSecret + payload;
        String computedSignature = computeHmacSha256(data);
        
        return MessageDigest.isEqual(
            computedSignature.getBytes(StandardCharsets.UTF_8),
            receivedSignature.getBytes(StandardCharsets.UTF_8)
        );
    }

    private String computeHmacSha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute HMAC-SHA256", e);
        }
    }
}
