package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.models.Tag;
import com.caykhe.itforum.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable = (page == null || size == null || page < 0 || size <= 0)
                ? Pageable.unpaged()
                : PageRequest.of(page, size, Sort.by("name"));

        List<Tag> tags = tagRepository.findAll(pageable).getContent();
        long count = tagRepository.count();

        return new ResultCount<>(tags, count);
    }
}
