package ru.otus.spring.hw26.moderator.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.otus.spring.hw26.moderator.domain.Moderate;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.mapper.Mapper;
import ru.otus.spring.hw26.moderator.mapper.ModerateMapper;
import ru.otus.spring.hw26.moderator.repository.ModerateRepository;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;
import ru.otus.spring.hw26.moderator.service.CrudService;
import ru.otus.spring.hw26.moderator.service.CrudServiceImpl;



@TestConfiguration
@ComponentScan(
        basePackages = "ru.otus.spring.hw26.moderator.config",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {KafkaSecurityConfig.class, CustomSecurityConfig.class})
)
public class ConfigTest {

    @Autowired
    private ModerateRepository moderateRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Mapper<ModerateDto, Moderate> moderateMapper(){
        return new ModerateMapper(objectMapper);
    }
    @Bean
    public CrudService<ModerateDto, ModerateSearch> moderateService(){
        return new CrudServiceImpl(moderateRepository, moderateMapper());
    }

}
