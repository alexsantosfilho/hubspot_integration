package com.hubspot.integration.service.contact;


import com.hubspot.integration.dto.request.ContactRequestDTO;
import com.hubspot.integration.dto.response.ContactResponseDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j

@Service
public class HubSpotContactService {

    @Value("${hubspot.api.base-url}")
    private String hubspotApiBaseUrl;

    @Value("${hubspot.api.contacts-endpoint}")
    private String contactsEndpoint;

    @Value("${hubspot.api.rate-limit}")
    private int rateLimit;

    @Value("${hubspot.api.rate-limit-interval}")
    private long rateLimitInterval;

    private final RestTemplate restTemplate;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private LocalDateTime lastResetTime = LocalDateTime.now();

    public HubSpotContactService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ContactResponseDTO createContact(ContactRequestDTO contactRequest, String accessToken) {
        checkRateLimit();

        String url = hubspotApiBaseUrl + contactsEndpoint;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<ContactRequestDTO> requestEntity = new HttpEntity<>(contactRequest, headers);
        
        try {
            ResponseEntity<ContactResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ContactResponseDTO.class);
            log.info("Resposta do HubSpot (raw): " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Error creating contact in HubSpot. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new RuntimeException("Rate limit excedido. Por favor, tente novamente mais tarde.");
            }
            throw new RuntimeException("Erro ao criar contato no HubSpot: " + e.getMessage());
        }
    }

    private synchronized void checkRateLimit() {
        LocalDateTime now = LocalDateTime.now();
        long secondsSinceLastReset = ChronoUnit.MILLIS.between(lastResetTime, now);

        if (secondsSinceLastReset > rateLimitInterval) {
            requestCount.set(0);
            lastResetTime = now;
        }

        if (requestCount.incrementAndGet() > rateLimit) {
            throw new RuntimeException("Rate limit excedido. Limite de " + rateLimit + 
                                      " requisições a cada " + (rateLimitInterval/1000) + " segundos.");
        }
    }
}