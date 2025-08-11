package ru.otus.spring.hw26.moderator.service.security;

import java.util.Map;

public interface CustomSecurityContextService {
       String getUserId();
       String getJwtToken();
       Map<String, String> getUserInfo();
}
