package ru.otus.spring.hw26.moderator.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomSecurityContextServiceImpl implements CustomSecurityContextService {
    @Override
    public String getUserId(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("sub");
    }

    @Override
    public String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null || ((Jwt) authentication.getCredentials()).getTokenValue() == null)
            return null;
        return ((Jwt) authentication.getCredentials()).getTokenValue();
    }

    @Override
    public Map<String, String> getUserInfo() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> userInfo = new HashMap<>();
        if(Objects.nonNull(jwt)) {
            userInfo.put("userId", jwt.getClaim("sub"));
            userInfo.put("firstname", jwt.getClaim("given_name"));
            userInfo.put("lastname", jwt.getClaim("family_name"));
            userInfo.put("email", jwt.getClaim("email"));
        }
        return userInfo;
    }
}
