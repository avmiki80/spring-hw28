package ru.otus.spring.hw26.moderator.service.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakTokenServiceImpl implements KeycloakTokenService {
    private final RestTemplate restTemplate;
    private final String tokenUrl;

    public KeycloakTokenServiceImpl(RestTemplate restTemplate, @Value("${tokenUrl}") String tokenUrl) {
        this.restTemplate = restTemplate;
        this.tokenUrl = tokenUrl;
    }
    @Override
    public String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "library");
//        map.add("client_secret", clientSecret);
        map.add("username", "admin");
        map.add("password", "admin");
        map.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                TokenResponse.class
        );

        return response.getBody().getAccessToken();
    }
    @Getter @Setter
    private static class TokenResponse {
        private String access_token;
        private String token_type;
        private int expires_in;
        private String refresh_token;
        public String getAccessToken() {
            return access_token;
        }

    }
}
