package com.hubspot.integration.service.contact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.integration.dto.contact.ContactRequestDTO;
import com.hubspot.integration.dto.contact.ContactResponseDTO;

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

            ContactResponseDTO responseDTO = response.getBody();

            if (responseDTO != null) {
                responseDTO.setSuccess(true); // Marca como sucesso
            } else {
                responseDTO = ContactResponseDTO.builder()
                        .success(true)
                        .message("Contato criado com sucesso.")
                        .build();
            }

            log.info("Contato criado com sucesso: {}", responseDTO);
            return responseDTO;

        } catch (HttpClientErrorException e) {
            log.error("Erro ao criar contato no HubSpot. Status: {}, Response: {}", e.getStatusCode(),
                    e.getResponseBodyAsString());

            String message = "Erro ao criar contato no HubSpot. Código: " + e.getStatusCode();

            try {
                ObjectMapper mapper = new ObjectMapper();
                String detailedMessage = mapper.readTree(e.getResponseBodyAsString())
                        .path("message")
                        .asText(null);
                if (detailedMessage != null) {
                    message += " - " + detailedMessage;
                }
            } catch (Exception parseException) {
                log.warn("Não foi possível extrair mensagem detalhada do erro: {}", parseException.getMessage());
            }

            return ContactResponseDTO.builder()
                    .success(false)
                    .message(message)
                    .build();
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
                    " requisições a cada " + (rateLimitInterval / 1000) + " segundos.");
        }
    }
}