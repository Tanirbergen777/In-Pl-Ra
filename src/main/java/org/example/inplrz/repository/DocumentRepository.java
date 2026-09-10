package org.example.inplrz.repository;

import org.example.inplrz.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByActivityIdAndDeletedAtIsNull(UUID activityId);
}