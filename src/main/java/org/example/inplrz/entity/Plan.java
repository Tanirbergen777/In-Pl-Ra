package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "idp_plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Многие планы могут принадлежать Одному сотруднику (Many-To-One)
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "plan_status_enum", nullable = false)
    private PlanStatus status = PlanStatus.DRAFT;

    @Column(nullable = false)
    private Integer version = 1;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}