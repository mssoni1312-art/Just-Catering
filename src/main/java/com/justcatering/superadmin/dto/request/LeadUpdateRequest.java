package com.justcatering.superadmin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.LeadStage;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Request payload for updating a meeting lead.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadUpdateRequest {

    /** Company name. */
    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String companyName;

    /** Owner / contact person name. */
    @Size(max = 200, message = "Owner name must not exceed 200 characters")
    private String ownerName;

    /** Legacy owner first name (optional). */
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    /** Legacy owner last name (optional). */
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    /** Owner email address (optional). */
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /** Business or contact address. */
    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    /** Phone number. */
    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Pattern(regexp = "^[+0-9\\s-]{7,20}$", message = "Phone number format is invalid")
    private String phone;

    /** State. */
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    /** City. */
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    /** Interested product name. */
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String productName;

    /** Interested product UUID (alternative to product name; pass null to clear). */
    private UUID productUuid;

    /** Approximate budget. */
    @NotNull(message = "Approximate budget is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Approximate budget must be zero or positive")
    private BigDecimal approxBudget;

    /** Optional notes. */
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    /** Lead pipeline stage. */
    private LeadStage leadStage;

    /** Entity status. */
    private EntityStatus status;

    /**
     * Validates that at least one owner-name field is provided.
     *
     * @return {@code true} when owner name is present
     */
    @AssertTrue(message = "Owner name is required")
    @JsonIgnore
    public boolean isOwnerNameProvided() {
        return StringUtils.hasText(resolveOwnerName());
    }

    /**
     * Resolves the owner name from ownerName or legacy first/last name fields.
     *
     * @return trimmed owner name, or {@code null} when absent
     */
    @JsonIgnore
    public String resolveOwnerName() {
        if (StringUtils.hasText(ownerName)) {
            return ownerName.trim();
        }

        String combined = ((firstName == null ? "" : firstName.trim()) + " "
                + (lastName == null ? "" : lastName.trim())).trim();
        return combined.isEmpty() ? null : combined;
    }
}
