package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "dict_activity_types")
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;
}