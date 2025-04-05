package com.hubspot.integration.controller.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hubspot.integration.service.oauth.HubspotOAuthService;

@Slf4j

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {
    
    private final HubspotOAuthService hubspotOAuthService;
    
    @GetMapping("/authorize")
    public String authorize() {
    log.info("Received contact wdadadrequest: " );
        return hubspotOAuthService.buildAuthorizationUrl();
    }
    
    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state) {
        System.out.println("Received code: " + code);
        System.out.println("Received state: " + state);
        return hubspotOAuthService.exchangeCodeForTokens(code, state).toString();
    }
}