package ru.otus.spring.hw26.moderator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.moderator.domain.Moderate;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.exception.ServiceException;
import ru.otus.spring.hw26.moderator.mapper.Mapper;
import ru.otus.spring.hw26.moderator.repository.ModerateRepository;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;

import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.hw26.moderator.dto.PageRequestUtil.createPageRequest;

@Service
@RequiredArgsConstructor
public class CrudServiceImpl implements CrudService<ModerateDto, ModerateSearch> {
    private final ModerateRepository moderateRepository;
    private final Mapper<ModerateDto, Moderate> mapper;
    @Override
    @Transactional
    public ModerateDto save(ModerateDto obj) {
        return mapper.toDto(moderateRepository.save(mapper.toEntity(obj)));
    }

    @Override
    public ModerateDto update(long id, ModerateDto obj) {
        throw new UnsupportedOperationException("The method is prohibited");

    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            moderateRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ModerateDto> findAll() {
        return moderateRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ModerateDto findById(long id) {
        return mapper.toDto(moderateRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));

    }

    @Override
    public Page<ModerateDto> findByParams(ModerateSearch params) {
        PageRequest pageRequest = createPageRequest(params);
        Page<Moderate> result = moderateRepository.findByParams(
                params.getText(),
                params.getFrom(),
                params.getTo(),
                pageRequest
        );
        return new PageImpl<>(
                result.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                pageRequest,
                result.getTotalElements()
        );
    }

    @Override
    public void saveAll(List<ModerateDto> objs) {
        List<Moderate> entities = objs.stream().map(mapper::toEntity).collect(Collectors.toList());;
        moderateRepository.saveAll(entities);
    }
}
