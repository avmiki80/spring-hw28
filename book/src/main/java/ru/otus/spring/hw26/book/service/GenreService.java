package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.book.domain.Genre;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.mapper.Mapper;
import ru.otus.spring.hw26.book.repository.GenreRepository;
import ru.otus.spring.hw26.book.dto.GenreSearch;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService implements CrudService<GenreDto, GenreSearch> {
    private final GenreRepository genreRepository;
    private final Mapper<GenreDto, Genre> genreMapper;

    @Override
    @Transactional
    public GenreDto save(GenreDto obj) {
        Genre genre = genreMapper.toEntity(obj);
        return genreMapper.toDto(genreRepository.save(genre));
    }

    @Override
    @Transactional
    public GenreDto update(long id, GenreDto obj) {
        Genre persistGenre =  genreRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found"));
        persistGenre.setTitle(obj.getTitle());
        return genreMapper.toDto(persistGenre);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            genreRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto findById(long id) {
        return genreMapper.toDto(genreRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }
    @Override
    public List<GenreDto> findByParams(GenreSearch params) {
        return genreRepository.findByParams(params.getTitle()).stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }
}
