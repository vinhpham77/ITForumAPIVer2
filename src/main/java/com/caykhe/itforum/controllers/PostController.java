package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.PostDto;
import com.caykhe.itforum.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/create")
    public ResponseEntity<?> create(PostDto postDto) {
        return new ResponseEntity<>(postService.create(postDto), HttpStatus.OK);
    }
}