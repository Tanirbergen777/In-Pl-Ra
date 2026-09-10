package org.example.inplrz.repository;

import org.example.inplrz.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByPlanId(UUID planId);
}