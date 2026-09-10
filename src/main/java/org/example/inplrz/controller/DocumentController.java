package org.example.inplrz.controller;

import org.example.inplrz.entity.Activity;
import org.example.inplrz.entity.Document;
import org.example.inplrz.entity.User;
import org.example.inplrz.repository.ActivityRepository;
import org.example.inplrz.repository.DocumentRepository;
import org.example.inplrz.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activities")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    // Папка для хранения файлов (создастся в корне твоего проекта)
    private final String UPLOAD_DIR = "uploads/";

    public DocumentController(DocumentRepository documentRepository, ActivityRepository activityRepository, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;

        // Автоматически создаем папку uploads, если ее еще нет
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Получение списка документов мероприятия
    @GetMapping("/{activityId}/documents")
    public ResponseEntity<List<Document>> getDocumentsByActivity(@PathVariable UUID activityId) {
        return ResponseEntity.ok(documentRepository.findByActivityIdAndDeletedAtIsNull(activityId));
    }

    // Загрузка документа к мероприятию
    @PostMapping("/{activityId}/documents")
    public ResponseEntity<?> uploadDocument(
            @PathVariable UUID activityId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploaderId") UUID uploaderId) {

        // 1. Проверка на пустой файл
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Ошибка: Файл пустой!");
        }

        // 2. Строгая проверка формата по ТЗ
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(fileName).toLowerCase();
        List<String> allowedExtensions = List.of("pdf", "jpg", "jpeg", "png", "docx");

        if (!allowedExtensions.contains(fileExtension)) {
            return ResponseEntity.badRequest().body("Недопустимый формат! Разрешены только: PDF, JPG, JPEG, PNG, DOCX");
        }

        try {
            // 3. Ищем мероприятие и пользователя (кто грузит)
            Activity activity = activityRepository.findById(activityId)
                    .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
            User uploader = userRepository.findById(uploaderId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            // 4. Сохраняем физический файл на сервер
            // Добавляем случайный UUID к имени, чтобы файлы с одинаковым названием не перезаписали друг друга
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            // 5. Сохраняем запись в базу данных
            Document document = new Document();
            document.setActivity(activity);
            document.setName(fileName);
            document.setFilePath(filePath.toString());
            document.setDocumentType(fileExtension);
            document.setUploadedBy(uploader);
            // Версия и дата проставятся автоматически (по умолчанию 1 и now())

            Document savedDocument = documentRepository.save(document);
            return ResponseEntity.ok(savedDocument);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при сохранении файла на сервере");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}