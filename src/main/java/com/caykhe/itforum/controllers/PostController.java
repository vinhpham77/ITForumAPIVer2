package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.PostDto;
import com.caykhe.itforum.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/by/{username}")
    public ResponseEntity<?> get(@PathVariable String username, Integer page, Integer size) {
        return new ResponseEntity<>(postService.getByUser(username, page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return new ResponseEntity<>(postService.get(id), HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.create(postDto), HttpStatus.OK);
    }
}