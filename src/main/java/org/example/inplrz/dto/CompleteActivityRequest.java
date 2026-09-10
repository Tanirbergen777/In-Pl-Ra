package org.example.inplrz.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CompleteActivityRequest {
    private LocalDate factEndDate;
    private String actualResult;
}