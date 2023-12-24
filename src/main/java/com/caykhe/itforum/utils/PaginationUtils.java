package com.caykhe.itforum.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PaginationUtils {
    public static Pageable getPageable(Integer page, Integer size, String ...properties) {
        if (page == null || size == null || page < 0 || size <= 0) {
            return Pageable.unpaged();
        } else if (properties.length == 0) {
            return PageRequest.of(page, size);
        } else {
            Sort sort = Arrays.stream(properties)
                    .filter(Objects::nonNull)
                    .map(property -> property.startsWith("-")
                            ? Sort.by(property.substring(1)).descending()
                            : Sort.by(property).ascending())
                    .reduce(Sort::and)
                    .orElse(Sort.unsorted());
            return PageRequest.of(page, size, sort);
        }
    }
}
