package com.aiyl.bank.rest.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long totalPages;
    private List<T> content;

    public static <T> PaginationResponse<T> of(Page<T> page) {
        return PaginationResponse.<T>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
    }
}

