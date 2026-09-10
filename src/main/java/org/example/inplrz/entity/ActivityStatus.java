package org.example.inplrz.entity;

public enum ActivityStatus {
    PLANNED,                // Запланировано
    IN_PROGRESS,            // В процессе
    ON_CONFIRMATION,        // На подтверждении
    DONE_AND_CONFIRMED,     // Выполнено и подтверждено
    NEEDS_REWORK,           // На доработке
    OVERDUE,                // Просрочено
    POSTPONED,              // Перенесено (в ТЗ опечатка "Перевезено")
    CANCELED,               // Отменено
    NOT_DONE                // Не выполнено
}