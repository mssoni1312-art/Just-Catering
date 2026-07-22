package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Client query / requirement entity.
 */
@Entity
@Table(name = "client_queries")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientQuery extends BaseEntity {

    /** Related client. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Query title. */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** Requirement summary shown below the title. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** Optional type (REQUIREMENT, DOCUMENT, COMPLIANCE, OTHER, or free text). */
    @Column(name = "query_type", length = 100)
    private String queryType;

    /** Assigned team member. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    /** Responsible department. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /** Priority level. */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    /** Workflow status. */
    @Enumerated(EnumType.STRING)
    @Column(name = "query_status", nullable = false, length = 30)
    @Builder.Default
    private QueryStatus queryStatus = QueryStatus.PENDING;

    /** Optional notes / remarks. */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    /** Optional image path. */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /** Scheduled requirement date and time. */
    @Column(name = "scheduled_at")
    private Instant scheduledAt;

    /** Whether check-in and check-out details are available. */
    @Column(name = "has_check_in_out", nullable = false)
    @Builder.Default
    private Boolean hasCheckInOut = Boolean.FALSE;

    /** Optional voice note URL. */
    @Column(name = "voice_url", length = 500)
    private String voiceUrl;

    /** Voice note duration in seconds. */
    @Column(name = "voice_duration_seconds")
    private Integer voiceDurationSeconds;

    /** Optional uploaded document URL. */
    @Column(name = "document_url", length = 500)
    private String documentUrl;

    /** Uploaded document display name. */
    @Column(name = "document_name", length = 255)
    private String documentName;

    /** Uploaded document size in bytes. */
    @Column(name = "document_size_bytes")
    private Long documentSizeBytes;

    /** Uploaded document MIME type. */
    @Column(name = "document_content_type", length = 100)
    private String documentContentType;

    /** Completion timestamp. */
    @Column(name = "completed_at")
    private Instant completedAt;
}
