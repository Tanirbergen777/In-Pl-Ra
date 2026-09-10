package org.example.inplrz.dto;

import lombok.Data;
import org.example.inplrz.entity.ActivityStatus;

@Data
public class UpdateActivityStatusRequest {
    private ActivityStatus status;
}