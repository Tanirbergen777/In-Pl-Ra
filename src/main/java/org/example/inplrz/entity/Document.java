package org.example.inplrz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents") // Имя таблицы строго из БД
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", columnDefinition = "text", nullable = false)
    private String filePath;

    @Column(name = "document_type", length = 50)
    private String documentType;

    @Column(name = "version")
    private Integer version = 1;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Метод, который Hibernate вызовет автоматически перед первым сохранением
    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}