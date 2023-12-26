package com.caykhe.itforum.controllers;

import com.caykhe.itforum.services.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    final BookmarkService bookmarkService;

    @GetMapping("/getPost")
    public ResponseEntity<?> getPostByUserName(@RequestParam String username,
                                               @RequestParam(required = false, name = "page") Integer page,
                                               @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit,
                                               @RequestParam(required = false, defaultValue = "") String tag){
        return new ResponseEntity<>(bookmarkService.getPostByUserName(username, page, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/getSeries")
    public ResponseEntity<?> getSeriesByUserName(@RequestParam String username,
                                                 @RequestParam(required = false, name = "page") Integer page,
                                                 @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit){
        return new ResponseEntity<>(bookmarkService.getSeriesByUserName(username, page, limit),HttpStatus.OK);
    }
}
