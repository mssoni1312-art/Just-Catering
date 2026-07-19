package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.FileUploadResponse;
import com.justcatering.superadmin.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for file upload and download APIs.
 */
@RestController
@RequestMapping(AppConstants.FILE_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Files", description = "File upload and download APIs")
public class FileUploadController {

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

    private final FileStorageService fileStorageService;

    /**
     * Uploads one or more files (images, PDF, or Word documents).
     *
     * @param files multipart files (use repeated {@code files} fields for multiple uploads)
     * @param file  single multipart file (backward compatible)
     * @return upload metadata for each stored file
     */
    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload one or more files")
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> upload(
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        List<MultipartFile> uploads = resolveUploads(files, file);
        List<FileUploadResponse> data = fileStorageService.storeAll(uploads);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data, HttpStatus.CREATED.value()));
    }

    private List<MultipartFile> resolveUploads(List<MultipartFile> files, MultipartFile file) {
        List<MultipartFile> uploads = new ArrayList<>();
        if (files != null) {
            uploads.addAll(files);
        }
        if (file != null && !file.isEmpty()) {
            uploads.add(file);
        }
        return uploads;
    }

    /**
     * Serves a previously uploaded file.
     *
     * @param fileName stored file name
     * @return file resource
     */
    @GetMapping("/{fileName}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Download or serve an uploaded file")
    public ResponseEntity<Resource> download(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadAsResource(fileName);
        String contentType = resolveContentType(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    private String resolveContentType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        return EXTENSION_CONTENT_TYPES.getOrDefault(extension, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
