package com.hubspot.integration.dto.oauth;

import lombok.Data;

@Data
public class OAuthState {
    private String state;
    private String redirectUri;
    private String userId;
}