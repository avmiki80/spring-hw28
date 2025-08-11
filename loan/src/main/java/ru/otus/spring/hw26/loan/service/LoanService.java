package ru.otus.spring.hw26.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.h26.model.book.BookDto;
import ru.otus.spring.hw26.loan.domain.LoanBook;
import ru.otus.spring.hw26.loan.dto.LoanDto;
import ru.otus.spring.hw26.loan.dto.LoanSearch;
import ru.otus.spring.hw26.loan.exception.ServiceException;
import ru.otus.spring.hw26.loan.mapper.Mapper;
import ru.otus.spring.hw26.loan.repository.LoanBookRepository;
import ru.otus.spring.hw26.loan.service.book.BookService;
import ru.otus.spring.hw26.loan.service.secure.CustomSecurityContextService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.spring.hw26.loan.dto.PageRequestUtil.createPageRequest;

@Service
@RequiredArgsConstructor
public class LoanService implements CrudService<LoanDto, LoanSearch> {
    private final BookService bookService;
    private final CustomSecurityContextService customSecurityContextService;
    private final LoanBookRepository loanBookRepository;
    private final Mapper<LoanDto, LoanBook> loanMapper;

    @Override
    @Transactional
    public LoanDto save(LoanDto obj) {
        LoanBook loanBook = loanMapper.toEntity(obj);
        loanBook.setReturnDate(LocalDate.now().plusDays(14));
        Map<String, String> userInfo = customSecurityContextService.getUserInfo();
        loanBook.setUserId(userInfo.get("userId"));
        loanBook.setFirstname(userInfo.get("firstname"));
        loanBook.setLastname(userInfo.get("lastname"));
        loanBook.setEmail(userInfo.get("email"));
        LoanBook persist = loanBookRepository.save(loanBook);
        LoanDto result = loanMapper.toDto(persist);
        BookDto bookDto = bookService.findById(loanBook.getBookId());
        result.setBookDto(bookDto);
        return result;
    }

    @Override
    @Transactional
    public LoanDto update(long id, LoanDto obj) {
        LoanBook loanBook = loanBookRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found"));
        loanBook.setDescription(obj.getDescription());
        loanBook.setReturnDate(obj.getReturnDate());
        LoanDto result = loanMapper.toDto(loanBook);
        BookDto bookDto = bookService.findById(loanBook.getBookId());
        result.setBookDto(bookDto);
        return result;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            loanBookRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> findAll() {
        return loanBookRepository.findAll().stream().map(loanMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDto findById(long id) {
        LoanBook loanBook = loanBookRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found"));
        LoanDto result = loanMapper.toDto(loanBook);
        BookDto bookDto = bookService.findById(loanBook.getBookId());
        result.setBookDto(bookDto);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanDto> findByParams(LoanSearch params) {
        PageRequest pageRequest = createPageRequest(params);
        Page<LoanBook> result = loanBookRepository.findByParams(
                params.getUserid(),
                params.getLoanDate(),
                params.getReturnDate(),
                params.getEmail(),
                params.getFirstname(),
                params.getLastname(),
                pageRequest
        );
        return new PageImpl<>(
                result.getContent().stream().map(loanMapper::toDto).peek(i -> i.setBookDto(bookService.findById(i.getBookId()))).collect(Collectors.toList()),
                pageRequest,
                result.getTotalElements()
        );
    }
}
