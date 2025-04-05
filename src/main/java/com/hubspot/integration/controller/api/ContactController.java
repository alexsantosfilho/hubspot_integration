package com.hubspot.integration.controller.api;

import com.hubspot.integration.dto.request.ContactRequestDTO;
import com.hubspot.integration.dto.response.ContactResponseDTO;
import com.hubspot.integration.service.contact.HubSpotContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final HubSpotContactService hubSpotContactService;

    public ContactController(HubSpotContactService hubSpotContactService) {
        this.hubSpotContactService = hubSpotContactService;
    }

    @PostMapping
    public ResponseEntity<ContactResponseDTO> createContact(
            @Valid @RequestBody ContactRequestDTO contactRequest,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        log.info("Recebida requisição para criar contato: {}", contactRequest);
        
        String accessToken = authorizationHeader.substring("Bearer ".length());
        ContactResponseDTO response = hubSpotContactService.createContact(contactRequest, accessToken);
        
        return ResponseEntity.ok(response);
    }
}