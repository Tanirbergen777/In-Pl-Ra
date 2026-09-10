package org.example.inplrz.specification;

import org.example.inplrz.entity.Plan;
import org.example.inplrz.entity.PlanStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class PlanSpecification {

    public static Specification<Plan> filterPlans(PlanStatus status, String employeeName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Фильтр по статусу плана (если передан)
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 2. Фильтр по Ф.И.О. сотрудника (поиск по части имени)
            if (employeeName != null && !employeeName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("employee").get("fullName")),
                        "%" + employeeName.toLowerCase() + "%"
                ));
            }

            // Объединяем все фильтры через AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}