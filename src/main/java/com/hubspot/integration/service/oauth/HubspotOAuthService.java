package com.hubspot.integration.service.oauth;


import com.hubspot.integration.dto.oauth.HubspotTokenResponse;
import com.hubspot.integration.exception.OAuthException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubspotOAuthService {
    
    private final RestTemplate restTemplate;
    
    @Value("${hubspot.client.id}")
    private String clientId;
    
    @Value("${hubspot.client.secret}")
    private String clientSecret;
    
    @Value("${hubspot.redirect.uri}")
    private String redirectUri;
    
    @Value("${hubspot.scopes}")
    private String scopes;
    
    @Value("${hubspot.auth.url}")
    private String authUrl;
    
    @Value("${hubspot.token.url}")
    private String tokenUrl;

    @Value("${hubspot.response_type}")
    private String response_type;

    
    public String buildAuthorizationUrl() {
        try {
            String state = UUID.randomUUID().toString();
        
            return new URIBuilder(authUrl)
                    .addParameter("client_id", clientId)
                    .addParameter("scope", URLEncoder.encode(scopes, StandardCharsets.UTF_8))
                    .addParameter("redirect_uri", redirectUri)
                    .addParameter("state", state)
                    .addParameter("response_type", response_type)
                    .build().toString();
            
        } catch (URISyntaxException e) {
            log.error("Error building authorization URL", e);
            throw new OAuthException("Error building authorization URL");
        }
    }
    
    public HubspotTokenResponse exchangeCodeForTokens(String code, String state) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        
        try {
            ResponseEntity<HubspotTokenResponse> response = restTemplate.postForEntity(
                    tokenUrl, request, HubspotTokenResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                HubspotTokenResponse tokenResponse = response.getBody();
                if (tokenResponse != null) {
                    tokenResponse.setExpirationTime(Instant.now().plusSeconds(tokenResponse.getExpiresIn()));
                    return tokenResponse;
                } else {
                    throw new OAuthException("Token response is null");
                }
            } else {
                throw new OAuthException("Failed to exchange code for tokens");
            }
        } catch (Exception e) {
            log.error("Error exchanging code for tokens", e);
            throw new OAuthException("Error exchanging code for tokens");
        }
    }
    
    @Cacheable(value = "tokens", key = "#refreshToken")
    public HubspotTokenResponse refreshTokens(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("refresh_token", refreshToken);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        
        try {
            ResponseEntity<HubspotTokenResponse> response = restTemplate.postForEntity(
                    tokenUrl, request, HubspotTokenResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                HubspotTokenResponse tokenResponse = response.getBody();
                if (tokenResponse != null) {
                    tokenResponse.setExpirationTime(Instant.now().plusSeconds(tokenResponse.getExpiresIn()));
                } else {
                    throw new OAuthException("Token response is null");
                }
                return tokenResponse;
            } else {
                throw new OAuthException("Failed to refresh tokens");
            }
        } catch (Exception e) {
            log.error("Error refreshing tokens", e);
            throw new OAuthException("Error refreshing tokens");
        }
    }
 
}