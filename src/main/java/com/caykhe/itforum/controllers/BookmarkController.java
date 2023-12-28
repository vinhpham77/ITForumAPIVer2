package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.BookmarkDetailRequest;
import com.caykhe.itforum.models.Bookmark;
import com.caykhe.itforum.models.BookmarkDetail;
import com.caykhe.itforum.services.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    final BookmarkService bookmarkService;

    @GetMapping("/getPost")
    public ResponseEntity<?> getPostByUserName(@RequestParam String username,
                                               @RequestParam(required = false, name = "page") Integer page,
                                               @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit,
                                               @RequestParam(required = false, defaultValue = "") String tag) {
        return new ResponseEntity<>(bookmarkService.getPostByUserName(username, page, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/getSeries")
    public ResponseEntity<?> getSeriesByUserName(@RequestParam String username,
                                                 @RequestParam(required = false, name = "page") Integer page,
                                                 @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit) {
        return new ResponseEntity<>(bookmarkService.getSeriesByUserName(username, page, limit), HttpStatus.OK);
    }

    @PostMapping("/createBookmark/{username}")
    public ResponseEntity<?> createBookmark(@PathVariable String username) {
        return new ResponseEntity<>(bookmarkService.createBookmark(username), HttpStatus.CREATED);
    }

    @PostMapping("/createBookmarkPost/{bookmarkId}")
    public ResponseEntity<?> createBookmarkPost(@PathVariable Integer bookmarkId, @RequestBody BookmarkDetailRequest request) {
        Optional<Bookmark> bookmark = bookmarkService.getBookmarkById(bookmarkId);
        if (bookmark.isPresent()) {
            BookmarkDetail bookmarkDetail = bookmarkService.addBookmarkDetail(bookmark.get(), request.getTargetId(), request.getType());
            return new ResponseEntity<>(bookmarkDetail, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Lỗi Không tìm thấy bookmark cần tạo", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bookmark/{username}")
    public ResponseEntity<?> bookmark(@PathVariable String username, @RequestBody BookmarkDetailRequest request) {
        return new ResponseEntity<>(bookmarkService.bookmark(username, request), HttpStatus.OK);
    }

    @DeleteMapping("/unBookmark")
    public ResponseEntity<?> unBookmark(@RequestParam String username,
                                        @RequestBody BookmarkDetailRequest bookmarkDetailRequest) {
        bookmarkService.unBookmark(username, bookmarkDetailRequest);
        return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
    }

    @PostMapping("/isBookmark")
    public Boolean isBookmark(
            @RequestParam String username,
            @RequestBody BookmarkDetailRequest bookmarkDetailRequest) {
        return bookmarkService.isBookmark(username, bookmarkDetailRequest);
    }
}
