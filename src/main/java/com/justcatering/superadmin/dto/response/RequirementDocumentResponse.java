package com.justcatering.superadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Uploaded document metadata for a requirement list item.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementDocumentResponse {

    private String fileName;
    private String contentType;
    private Long sizeBytes;
    private String url;
    private String meta;
}
