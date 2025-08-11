package ru.otus.spring.hw26.book.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.book.domain.Audit;
import ru.otus.spring.hw26.book.dto.BaseDto;
import ru.otus.spring.hw26.book.repository.AuditRepository;
import ru.otus.spring.hw26.book.service.CustomSecurityContextService;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditingAspect {
    private final AuditRepository auditRepository;
    private final CustomSecurityContextService customSecurityContextService;
    @AfterReturning(
            pointcut = "@annotation(ru.otus.spring.hw26.book.aspect.Auditing)",
            returning = "result")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterReturningProcessAudit(JoinPoint joinPoint, Object result){
        try {
            Audit audit = new Audit();
            audit.setUserId(customSecurityContextService.getUserId());
            audit.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            audit.setIsCompleted(true);
            audit.setBoId(getId(result));
            auditRepository.save(audit);
        } catch (Exception e) {
            log.error("Audit failed for method {}: {}", joinPoint.getSignature(), e.getMessage());
        }
    }
    @AfterThrowing(
            pointcut = "@annotation(ru.otus.spring.hw26.book.aspect.Auditing)",
            throwing = "error"
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterThrowingProcessAudit(JoinPoint joinPoint, Throwable error){
        try {
            final Audit audit = new Audit();
            audit.setUserId(customSecurityContextService.getUserId());
            audit.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            audit.setIsCompleted(false);
            audit.setDescription(error.getMessage());
            auditRepository.save(audit);
        } catch (Exception e) {
            log.error("Audit failed for method {}: {}", joinPoint.getSignature(), e.getMessage());
        }
    }

    @SneakyThrows
    private Long getId(Object obj){
        return (obj instanceof BaseDto) ? ((BaseDto) obj).getId() : 0L;
    }
}
