package com.justcatering.superadmin.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.justcatering.superadmin.enums.ClientStage;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for updating a client.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateRequest {

    /** Client / company name. */
    @NotBlank(message = "Client name is required")
    @Size(max = 200, message = "Client name must not exceed 200 characters")
    private String clientName;

    /** Contact person. */
    @NotBlank(message = "Contact person is required")
    @Size(max = 150, message = "Contact person must not exceed 150 characters")
    private String contactPerson;

    /** Mobile number. */
    @JsonProperty("mobileNumber")
    @JsonAlias("mobile")
    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    @Pattern(regexp = "^$|^[+0-9\\s-]{7,20}$", message = "Mobile number format is invalid")
    private String mobileNumber;

    /** Email address. */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /** Optional GST number. */
    @Size(max = 20, message = "GST number must not exceed 20 characters")
    private String gstNumber;

    /** Client business type (e.g. Just Catering). */
    @JsonProperty("type")
    @JsonAlias("clientType")
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Type must not exceed 100 characters")
    private String clientType;

    /** Selected purchased product UUID. */
    @JsonProperty("purchasedProductId")
    @JsonAlias({"productUuid", "product"})
    @NotNull(message = "Purchased product is required")
    private UUID purchasedProductId;

    /** Conversion / deal date. */
    @JsonProperty("conversionDate")
    @JsonAlias({"dealDate", "date"})
    private LocalDate conversionDate;

    /** Reward amount. */
    @JsonProperty("reward")
    @JsonAlias("totalAmount")
    @NotNull(message = "Reward is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Reward must be zero or positive")
    private BigDecimal reward;

    /** Optional budget. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Budget must be zero or positive")
    private BigDecimal budget;

    /** Optional notes. */
    @Size(max = 300, message = "Notes must not exceed 300 characters")
    private String notes;

    /** Client stage. */
    private ClientStage clientStage;

    /** Priority. */
    private Priority priority;

    /** Requirements completion percentage. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Requirements percentage must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Requirements percentage must not exceed 100")
    private BigDecimal requirementsCompletionPercentage;

    /** Optional state. */
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    /** Optional city. */
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    /** Entity status. */
    private EntityStatus status;
}
