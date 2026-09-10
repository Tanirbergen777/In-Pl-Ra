package org.example.inplrz.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreatePlanRequest {
    private UUID employeeId;
    private LocalDate periodStart;
    private LocalDate periodEnd;

}
