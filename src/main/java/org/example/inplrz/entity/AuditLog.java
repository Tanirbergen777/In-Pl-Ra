package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action_type", length = 50, nullable = false)
    private String actionType;

    // Храним изменения в формате JSON-строки
    @Column(name = "changed_fields", columnDefinition = "jsonb")
    private String changedFields;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    @Column(name = "plan_version")
    private Integer planVersion;

    @Column(name = "action_time")
    private LocalDateTime actionTime;

    @PrePersist
    protected void onCreate() {
        this.actionTime = LocalDateTime.now();
    }
}