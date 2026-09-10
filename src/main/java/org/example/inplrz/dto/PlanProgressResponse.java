package org.example.inplrz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlanProgressResponse {
    private long totalActivities;     // Общее количество (без отмененных)
    private long completedActivities; // Сколько выполнено
    private int percentage;           // Процент выполнения (0-100)
}