package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.response.FileUploadResponse;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * File storage service contract for upload and retrieval.
 */
public interface FileStorageService {

    /**
     * Stores an uploaded file on disk.
     *
     * @param file multipart upload
     * @return upload metadata
     */
    FileUploadResponse store(MultipartFile file);

    /**
     * Stores multiple uploaded files on disk.
     *
     * @param files multipart uploads
     * @return upload metadata for each file
     */
    List<FileUploadResponse> storeAll(List<MultipartFile> files);

    /**
     * Loads a stored file as a resource.
     *
     * @param fileName stored file name
     * @return file resource
     */
    Resource loadAsResource(String fileName);
}
