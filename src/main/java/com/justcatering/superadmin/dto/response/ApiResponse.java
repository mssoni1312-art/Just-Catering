package com.justcatering.superadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Standard API response wrapper returned by every endpoint.
 *
 * @param <T> payload type
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** Indicates whether the operation succeeded. */
    private Boolean success;

    /** Human-readable message. */
    private String message;

    /** Response payload. */
    private T data;

    /** Validation or business error details. */
    private List<ErrorDetail> errors;

    /** Response generation timestamp. */
    private Instant timestamp;

    /** HTTP status code. */
    private Integer statusCode;

    /**
     * Builds a successful response with data.
     *
     * @param message success message
     * @param data    payload
     * @param status  HTTP status
     * @param <T>     payload type
     * @return api response
     */
    public static <T> ApiResponse<T> success(String message, T data, int status) {
        return ApiResponse.<T>builder()
                .success(Boolean.TRUE)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .statusCode(status)
                .build();
    }

    /**
     * Builds a successful response without data.
     *
     * @param message success message
     * @param status  HTTP status
     * @param <T>     payload type
     * @return api response
     */
    public static <T> ApiResponse<T> success(String message, int status) {
        return success(message, null, status);
    }

    /**
     * Builds a failure response.
     *
     * @param message failure message
     * @param errors  error details
     * @param status  HTTP status
     * @param <T>     payload type
     * @return api response
     */
    public static <T> ApiResponse<T> failure(String message, List<ErrorDetail> errors, int status) {
        return ApiResponse.<T>builder()
                .success(Boolean.FALSE)
                .message(message)
                .errors(errors)
                .timestamp(Instant.now())
                .statusCode(status)
                .build();
    }

    /**
     * Builds a failure response without detailed errors.
     *
     * @param message failure message
     * @param status  HTTP status
     * @param <T>     payload type
     * @return api response
     */
    public static <T> ApiResponse<T> failure(String message, int status) {
        return failure(message, null, status);
    }
}
