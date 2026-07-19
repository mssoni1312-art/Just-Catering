package com.justcatering.superadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Individual error detail included in {@link ApiResponse}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    /** Field name when the error is field-specific. */
    private String field;

    /** Error message. */
    private String message;

    /** Optional machine-readable error code. */
    private String code;
}
