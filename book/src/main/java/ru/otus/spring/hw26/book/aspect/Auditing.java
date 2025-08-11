package ru.otus.spring.hw26.book.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Аннотация применяется к методам
@Retention(RetentionPolicy.RUNTIME) // Доступна в runtime через reflection
public @interface Auditing {
}
