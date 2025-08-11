package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.book.aspect.Auditing;
import ru.otus.spring.hw26.book.domain.Author;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.mapper.Mapper;
import ru.otus.spring.hw26.book.repository.AuthorRepository;
import ru.otus.spring.hw26.book.dto.AuthorSearch;
import ru.otus.spring.hw26.book.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<AuthorDto, AuthorSearch> {
    private final AuthorRepository authorRepository;
    private final Mapper<AuthorDto, Author> authorMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    @Auditing
    public AuthorDto save(AuthorDto obj) {
        Author author = authorMapper.toEntity(obj);
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Override
    @Transactional
    @Auditing
    public AuthorDto update(long id, AuthorDto obj) {
        Author persistAuthor = authorRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found"));
        persistAuthor.setFirstname(obj.getFirstname());
        persistAuthor.setLastname(obj.getLastname());
        return authorMapper.toDto(persistAuthor);
    }


    @Override
    @Transactional
    @Auditing
    public void deleteById(long id) {
        try {
            if(bookRepository.findByAuthorId(id).isEmpty())
                authorRepository.deleteById(id);
            else
                throw new ServiceException("Нельзя удалить автора связаного с книгой");
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findById(long id) {
        return authorMapper.toDto(authorRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }
    @Override
    public List<AuthorDto> findByParams(AuthorSearch params) {
        return authorRepository.findByParams(params.getFirstname(), params.getLastname()).stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }
}
