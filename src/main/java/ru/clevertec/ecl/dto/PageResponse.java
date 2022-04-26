package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageResponse<T> {

    Metadata metadata;
    List<T> content;

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                Metadata.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .build(),
                page.getContent());
    }

    @Value
    @Builder
    public static class Metadata {
        int page;
        int size;
        long totalElements;
    }

}
