package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime; // Не забудь этот импорт!
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String department;

    @Column(name = "manager_id")
    private UUID managerId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "user_role_enum", nullable = false)
    private UserRole role = UserRole.EMPLOYEE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}