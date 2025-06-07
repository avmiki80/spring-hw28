package ru.otus.spring.hw26.moderator.mapper;


public interface Mapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
