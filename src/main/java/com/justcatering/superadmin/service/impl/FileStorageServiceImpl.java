package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.config.FileProperties;
import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.response.FileUploadResponse;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Disk-based implementation of {@link FileStorageService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "webp", "gif", "pdf", "doc", "docx"
    );

    private static final Map<String, String> EXTENSION_CONTENT_TYPES = Map.ofEntries(
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("png", "image/png"),
            Map.entry("webp", "image/webp"),
            Map.entry("gif", "image/gif"),
            Map.entry("pdf", "application/pdf"),
            Map.entry("doc", "application/msword"),
            Map.entry("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    );

    private final FileProperties fileProperties;

    private Path uploadPath;

    /**
     * Ensures the upload directory exists on startup.
     */
    @PostConstruct
    void init() {
        uploadPath = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not create upload directory: " + uploadPath, ex);
        }
        log.info("File upload directory initialized at {}", uploadPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileUploadResponse store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File is required");
        }
        return storeFile(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileUploadResponse> storeAll(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BusinessException("At least one file is required");
        }

        List<FileUploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            responses.add(storeFile(file));
        }

        if (responses.isEmpty()) {
            throw new BusinessException("At least one file is required");
        }

        return responses;
    }

    private FileUploadResponse storeFile(MultipartFile file) {
        if (file.getSize() > fileProperties.getMaxSizeBytes()) {
            throw new BusinessException(
                    "File size exceeds maximum allowed size of " + fileProperties.getMaxSizeBytes() + " bytes"
            );
        }

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );
        if (originalName.contains("..")) {
            throw new BusinessException("Invalid file name");
        }

        String extension = extractExtension(originalName);
        validateExtension(extension);

        String contentType = resolveContentType(file.getContentType(), extension);
        validateContentType(contentType);

        String storedFileName = UUID.randomUUID() + "." + extension;
        Path target = uploadPath.resolve(storedFileName).normalize();
        if (!target.startsWith(uploadPath)) {
            throw new BusinessException("Invalid file path");
        }

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BusinessException("Failed to store file: " + originalName);
        }

        log.info("Stored uploaded file as {}", storedFileName);

        return FileUploadResponse.builder()
                .fileName(storedFileName)
                .originalName(originalName)
                .contentType(contentType)
                .size(file.getSize())
                .url(AppConstants.FILE_API + "/" + storedFileName)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource loadAsResource(String fileName) {
        validateStoredFileName(fileName);
        Path filePath = uploadPath.resolve(fileName).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new BusinessException("Invalid file name");
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new EntityNotFoundException("File", fileName);
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new BusinessException("Invalid file path");
        }
    }

    private void validateStoredFileName(String fileName) {
        if (!StringUtils.hasText(fileName) || fileName.contains("..") || fileName.contains("/")) {
            throw new BusinessException("Invalid file name");
        }
        String extension = extractExtension(fileName);
        validateExtension(extension);
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            throw new BusinessException("File extension is required");
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    private void validateExtension(String extension) {
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("File type not allowed: ." + extension);
        }
    }

    private void validateContentType(String contentType) {
        if (!fileProperties.getAllowedContentTypes().contains(contentType)) {
            throw new BusinessException("Content type not allowed: " + contentType);
        }
    }

    private String resolveContentType(String uploadedContentType, String extension) {
        if (StringUtils.hasText(uploadedContentType)
                && fileProperties.getAllowedContentTypes().contains(uploadedContentType)) {
            return uploadedContentType;
        }
        return EXTENSION_CONTENT_TYPES.getOrDefault(extension, "application/octet-stream");
    }
}
