package com.justcatering.superadmin.util;

import com.justcatering.superadmin.constants.AppConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * Utility helpers for building Spring Data {@link Pageable} instances.
 */
public final class PageableUtil {

    private PageableUtil() {
        // Utility class
    }

    /**
     * Builds a pageable with safe defaults and maximum page size enforcement.
     *
     * @param page      zero-based page index
     * @param size      page size
     * @param sortBy    sort property (defaults to createdAt)
     * @param direction ASC or DESC (defaults to DESC)
     * @return pageable
     */
    public static Pageable of(Integer page, Integer size, String sortBy, String direction) {
        int pageNumber = page == null || page < 0 ? AppConstants.DEFAULT_PAGE : page;
        int pageSize = size == null || size < 1 ? AppConstants.DEFAULT_PAGE_SIZE : size;
        if (pageSize > AppConstants.MAX_PAGE_SIZE) {
            pageSize = AppConstants.MAX_PAGE_SIZE;
        }

        String property = StringUtils.hasText(sortBy) ? sortBy : "createdAt";
        Sort.Direction sortDirection = "ASC".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, property));
    }
}
