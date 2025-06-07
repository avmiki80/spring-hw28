package ru.otus.spring.hw26.book.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw26.book.service.ActuatorService;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Endpoint(id = "appinfo")
public class AppInfoActuatorEndpoint {
    private final ActuatorService actuatorService;
    @ReadOperation
    public Map<String, Long> appInfoEndpoint() {
        return actuatorService.getAppInfo();
    }
}
