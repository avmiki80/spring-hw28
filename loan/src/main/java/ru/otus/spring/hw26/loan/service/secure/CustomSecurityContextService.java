package ru.otus.spring.hw26.loan.service.secure;

import java.util.Map;

public interface CustomSecurityContextService {
       String getUserId();
       String getJwtToken();
       Map<String, String> getUserInfo();
}
