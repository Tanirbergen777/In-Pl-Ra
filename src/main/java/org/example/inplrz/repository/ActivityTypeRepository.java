package org.example.inplrz.repository;

import org.example.inplrz.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer> {
}