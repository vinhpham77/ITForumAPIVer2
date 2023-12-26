package com.caykhe.itforum.utils;

import com.caykhe.itforum.dtos.UserLatestPageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Objects;

public class PaginationUtils {
    public static Pageable getPageable(Integer page, Integer size, String... properties) {
        if (page == null || size == null || page < 1 || size < 1) {
            return Pageable.unpaged();
        } else if (properties.length == 0) {
            return PageRequest.of(page - 1, size);
        } else {
            Sort sort = Arrays.stream(properties)
                    .filter(Objects::nonNull)
                    .map(property -> property.startsWith("-")
                            ? Sort.by(property.substring(1)).descending()
                            : Sort.by(property).ascending())
                    .reduce(Sort::and)
                    .orElse(Sort.unsorted());
            return PageRequest.of(page - 1, size, sort);
        }
    }

    public static UserLatestPageable getUserLatestPageable(Integer page, Integer size) {
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();

        Pageable pageable = (page == null || size == null || page < 1 || size < 1)
                ? Pageable.unpaged()
                : PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        return new UserLatestPageable(requester, pageable);
    }
}
