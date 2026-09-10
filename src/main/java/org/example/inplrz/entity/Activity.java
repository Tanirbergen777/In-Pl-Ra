package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "idp_activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;



    @ManyToOne
    @JoinColumn(name = "competency_id", nullable = false)
    private Competence competence;


    @ManyToOne
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType type;

    @Column(name = "title",nullable = false)
    private String name;

    @Column(name = "planned_end_date",nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "activity_status_enum", nullable = false)
    private ActivityStatus status = ActivityStatus.PLANNED;

    @Column(name = "planned_start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "fact_end_date")
    private LocalDate factEndDate;

    @Column(name = "actual_result", columnDefinition = "text")
    private String actualResult;

    @Column(name = "manager_comment", columnDefinition = "text")
    private String managerComment;
}