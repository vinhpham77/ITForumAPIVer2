package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.models.Tag;
import com.caykhe.itforum.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag findByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new ApiException("Thẻ #" + name + " không tồn tại", HttpStatus.NOT_FOUND));
    }

    public ResultCount<Tag> get(Integer page, Integer size) {
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 0 : size;

        List<Tag> tags;
        if (pageNumber < 0 || pageSize < 0) {
            throw new ApiException("Trang hoặc kích thước trang không hợp lệ", HttpStatus.BAD_REQUEST);
        } else if (pageNumber == 0 && pageSize == 0) {
            tags = tagRepository.findAll(Sort.by("name"));
        } else {
            tags = tagRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("name"))).toList();
        }

        long count = tagRepository.count();

        return new ResultCount<>(tags, count);
    }
}
