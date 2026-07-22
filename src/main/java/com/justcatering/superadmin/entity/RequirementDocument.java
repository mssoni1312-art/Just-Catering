package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Uploaded document attached to an on-site requirement.
 */
@Entity
@Table(name = "requirement_documents")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementDocument extends BaseEntity {

    /** Parent requirement. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    /** Original file name. */
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    /** Stored file URL. */
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    /** File size in bytes. */
    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    /** MIME content type. */
    @Column(name = "content_type", length = 100)
    private String contentType;
}
