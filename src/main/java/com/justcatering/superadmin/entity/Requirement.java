package com.justcatering.superadmin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * On-site client requirement with check-in/out location tracking.
 */
@Entity
@Table(name = "requirements")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Requirement extends BaseEntity {

    /** Related client. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Requirement title. */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** Requirement summary. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** On-site notes. */
    @Column(name = "notes", length = 1000)
    private String notes;

    /** Assigned team member. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    /** Whether check-in is enabled. */
    @Column(name = "check_in_enabled", nullable = false)
    @Builder.Default
    private Boolean checkInEnabled = Boolean.FALSE;

    /** Check-in timestamp. */
    @Column(name = "check_in_at")
    private Instant checkInAt;

    /** Check-in latitude. */
    @Column(name = "check_in_latitude", precision = 10, scale = 7)
    private BigDecimal checkInLatitude;

    /** Check-in longitude. */
    @Column(name = "check_in_longitude", precision = 10, scale = 7)
    private BigDecimal checkInLongitude;

    /** Check-in address label. */
    @Column(name = "check_in_address", length = 500)
    private String checkInAddress;

    /** Whether check-out is enabled. */
    @Column(name = "check_out_enabled", nullable = false)
    @Builder.Default
    private Boolean checkOutEnabled = Boolean.FALSE;

    /** Check-out timestamp. */
    @Column(name = "check_out_at")
    private Instant checkOutAt;

    /** Check-out latitude. */
    @Column(name = "check_out_latitude", precision = 10, scale = 7)
    private BigDecimal checkOutLatitude;

    /** Check-out longitude. */
    @Column(name = "check_out_longitude", precision = 10, scale = 7)
    private BigDecimal checkOutLongitude;

    /** Check-out address label. */
    @Column(name = "check_out_address", length = 500)
    private String checkOutAddress;

    /** Optional voice note URL. */
    @Column(name = "voice_url", length = 500)
    private String voiceUrl;

    /** Voice note duration in seconds. */
    @Column(name = "voice_duration_seconds")
    private Integer voiceDurationSeconds;

    /** Original voice recording file name. */
    @Column(name = "voice_file_name", length = 255)
    private String voiceFileName;

    /** Voice recording MIME type. */
    @Column(name = "voice_content_type", length = 100)
    private String voiceContentType;

    /** Uploaded documents. */
    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<RequirementDocument> documents = new ArrayList<>();
}
