package ru.otus.spring.hw26.moderator.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.moderator.config.ConfigTest;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.exception.ServiceException;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс CrudServiceImpl для работы с результатами модерации коментариев")
class CrudServiceImplTest {
    @Autowired
    private CrudService<ModerateDto, ModerateSearch> moderateService;
    private final static long EXISTING_MODERATE_ID = 1L;
    private final static long UNEXISTING_MODERATE_ID = 10000L;
    private final static String EXISTING_MODERATE_TEXT = "CommentTest1";

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(moderateService).isNotNull();
    }

    @Test
    public void whenFindFromAndTo_thenShouldReturnFilteredModerates(){
        ModerateDto expectedModerate = ModerateDto.builder()
                .id(3L)
                .text("CommentTest3")
                .commentId(2L)
                .moderationTime(
                        LocalDateTime.of(
                                LocalDate.of(2025, 03, 29),
                                LocalTime.of(20, 26, 38)))
                .build();
        assertAll(
                () -> assertThat(moderateService.findByParams(
                        ModerateSearch.builder()
                                .from(LocalDateTime.of(
                                        LocalDate.of(2025, 03, 29),
                                        LocalTime.of(20, 25, 37)))
                                .to(LocalDateTime.of(
                                        LocalDate.of(2025, 03, 29),
                                        LocalTime.of(20, 26, 39)))
                                .build()))
                        .usingFieldByFieldElementComparator()
                        .contains(expectedModerate)
        );
    }
    @Test
    public void shouldSaveModerate(){
        ModerateDto result = moderateService.save(ModerateDto.builder()
                .text(EXISTING_MODERATE_TEXT)
                .commentId(1L).build());
        assertAll(
                () -> assertThat(result.getModerationTime()).isNotNull(),
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getCommentId()).isEqualTo(1L),
                () -> assertThat(result.getText()).isEqualTo(EXISTING_MODERATE_TEXT)
        );
    }
    @Test
    public void whenUpdateModerate_thenCorrectResult(){
        assertAll(
                () -> {
                    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> moderateService.update(1L, ModerateDto.builder().build()));
                    assertThat(exception.getMessage()).isEqualTo("The method is prohibited");
                }
        );
    }

    @Test
    public void whenGetModerate_thenCorrectResult(){
        ModerateDto expectedModerate = ModerateDto.builder()
                .id(EXISTING_MODERATE_ID)
                .text(EXISTING_MODERATE_TEXT)
                .commentId(1L)
                .build();
        ModerateDto actualModerate = moderateService.findById(expectedModerate.getId());
        assertThat(actualModerate).usingRecursiveComparison().ignoringFields("moderationTime").isEqualTo(expectedModerate);
    }

    @Test
    public void whenGetModerateWithUnexistedId_thenThrowsException(){
        assertThatThrownBy(() -> moderateService.findById(UNEXISTING_MODERATE_ID)).isInstanceOf(ServiceException.class);
    }

    @Test
    public void whenDeleteModerate_thenThrowsExceptionIfGeModerateAgain(){
        assertAll(
                () -> assertDoesNotThrow(() -> moderateService.deleteById(EXISTING_MODERATE_ID)),
                () -> assertThatThrownBy(() -> moderateService.findById(EXISTING_MODERATE_ID)).isInstanceOf(ServiceException.class)
        );
    }
}