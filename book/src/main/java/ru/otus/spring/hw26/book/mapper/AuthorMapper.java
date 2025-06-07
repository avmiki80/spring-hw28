package ru.otus.spring.hw26.book.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw26.book.domain.Author;
import ru.otus.spring.hw26.book.dto.AuthorDto;

@Component
@RequiredArgsConstructor
public class AuthorMapper implements Mapper<AuthorDto, Author>{
    private final ObjectMapper objectMapper;
    @Override
    public AuthorDto toDto(Author entity) {
        return objectMapper.convertValue(entity, AuthorDto.class);
    }

    @Override
    public Author toEntity(AuthorDto dto) {
        return objectMapper.convertValue(dto, Author.class);
    }
}
