package com.caykhe.itforum.controllers;

import com.caykhe.itforum.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> get(Integer page, Integer size) {
        return new ResponseEntity<>(tagService.get(page, size), HttpStatus.OK);
    }

}