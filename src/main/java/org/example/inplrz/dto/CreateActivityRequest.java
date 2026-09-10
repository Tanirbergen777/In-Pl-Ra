package org.example.inplrz.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateActivityRequest {
    private UUID planId;        // ID плана Олжаса (8128455f...)
    private Integer competenceId;  // Какую компетенцию качаем (например, 1 - Hard Skills)
    private Integer typeId;        // Как качаем (например, 2 - Онлайн-курс)
    private String name;        // Название: "Изучить Spring Boot"
    private LocalDate deadline; // До какого числа нужно сделать
    private LocalDate startDate;
}