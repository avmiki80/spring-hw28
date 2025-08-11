package ru.otus.spring.hw26.book.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audit")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "bo_id", nullable = false)
    private Long boId;

    @Column(name = "description")
    private String description;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @CreationTimestamp
    @Column(name = "action_time", updatable = false)
    private LocalDateTime actionTime;
}
