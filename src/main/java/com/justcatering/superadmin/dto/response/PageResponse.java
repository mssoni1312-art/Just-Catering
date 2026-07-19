package com.justcatering.superadmin.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

/**
 * Reusable pagination wrapper for list endpoints.
 *
 * @param <T> item type
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /** Page content. */
    private List<T> content;

    /** Zero-based page index. */
    private int page;

    /** Page size. */
    private int size;

    /** Total number of elements. */
    private long totalElements;

    /** Total number of pages. */
    private int totalPages;

    /** Whether this is the first page. */
    private boolean first;

    /** Whether this is the last page. */
    private boolean last;

    /**
     * Maps a Spring Data {@link Page} into a {@link PageResponse}.
     *
     * @param page Spring Data page
     * @param <T>  item type
     * @return page response
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
