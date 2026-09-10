package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "dict_competencies")
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY означает, что БД сама генерирует ID (1, 2, 3...)
    private Integer id;

    @Column(name = "name",nullable = false)
    private String name;
}