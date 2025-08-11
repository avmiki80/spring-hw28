package ru.otus.spring.hw26.book.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw26.book.service.CustomSecurityContextService;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements ClientHttpRequestInterceptor {

    private final CustomSecurityContextService customSecurityContextService;
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String jwtToken = customSecurityContextService.getJwtToken();

        if (jwtToken != null) {
            httpRequest.getHeaders().add("Authorization", "Bearer " + jwtToken);
        }

        return clientHttpRequestExecution.execute(httpRequest, body);    }
}
