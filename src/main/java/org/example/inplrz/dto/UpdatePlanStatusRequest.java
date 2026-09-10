package org.example.inplrz.dto;

import lombok.Data;
import org.example.inplrz.entity.PlanStatus;

@Data
public class UpdatePlanStatusRequest {
    private PlanStatus status;
}
