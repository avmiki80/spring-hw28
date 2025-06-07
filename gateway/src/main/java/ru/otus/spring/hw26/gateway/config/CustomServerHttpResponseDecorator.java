package ru.otus.spring.hw26.gateway.config;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CustomServerHttpResponseDecorator extends ServerHttpResponseDecorator {
    private final ServerHttpRequest request;
    public CustomServerHttpResponseDecorator(ServerHttpRequest request, ServerHttpResponse delegate) {
        super(delegate);
        this.request = request;
    }
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (getStatusCode() == HttpStatus.FORBIDDEN) {
            return createResponse(String.format("Доступ к ресурсу %s закрыт, не хватает прав.", request.getPath()));
        }
        if (getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return createResponse(String.format("Доступ к ресурсу %s закрыт, нет credential.", request.getPath()));
        }
//        if (Objects.requireNonNull(getStatusCode()).isError()) {
//            return createResponse(String.format("Ошибка доступа к ресурсу %s.", request.getPath()));
//        }
        return super.writeWith(body);
    }

    private Mono<Void> createResponse(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = bufferFactory().wrap(bytes);
        getHeaders().setContentType(MediaType.TEXT_PLAIN);
        getHeaders().setContentLength(bytes.length);
        return super.writeWith(Mono.just(buffer));
    }
}
