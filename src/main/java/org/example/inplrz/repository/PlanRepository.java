package org.example.inplrz.repository;

import org.example.inplrz.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- Проверь импорт

import java.util.List;
import java.util.UUID;

// Добавь JpaSpecificationExecutor<Plan> сюда:
public interface PlanRepository extends JpaRepository<Plan, UUID>, JpaSpecificationExecutor<Plan> {
    List<Plan> findByEmployeeId(UUID employeeId);
}