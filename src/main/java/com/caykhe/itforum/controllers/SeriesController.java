package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.SeriesDto;
import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.services.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/series")
public class SeriesController {
    private final SeriesService seriesService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return new ResponseEntity<>(seriesService.getDto(id), HttpStatus.OK);
    }

    @GetMapping("/by/{username}")
    public ResponseEntity<?> getByUser(@PathVariable String username, Integer page, Integer size) {
        return new ResponseEntity<>(seriesService.getByUser(username, page, size), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody SeriesDto seriesDto) {
        return new ResponseEntity<>(seriesService.create(seriesDto), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody SeriesDto seriesDto) {
        return new ResponseEntity<>(seriesService.update(id, seriesDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        seriesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(seriesService.getSeries(page, limit));
    }

    @GetMapping("/get/follow")
    public ResponseEntity<?> getPostAggregationsFollow(@RequestParam(required = false, name = "page") Integer page,
                                                       @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit) {
        return new ResponseEntity<>(seriesService.getSeriesFollow(page, limit), HttpStatus.OK);
    }

    @GetMapping("/detail/{seriesId}")
    public ResponseEntity<?> getPostsBySeriesId(@PathVariable Integer seriesId) {
        List<Post> postList = seriesService.getListPost(seriesId);
        if (postList.isEmpty()) {
            return new ResponseEntity<>("Không thấy bài viết nào trong series", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(postList, HttpStatus.OK);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSearch(@RequestParam(required = false, name = "searchField", defaultValue = "") String fieldName,
                                       @RequestParam(required = false, name = "search") String searchContent,
                                       @RequestParam(required = false, name = "sort", defaultValue = "") String sort,
                                       @RequestParam(required = false, name = "sortField", defaultValue = "") String sortField,
                                       @RequestParam(required = false, name = "page") Integer page,
                                       @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) {

        return new ResponseEntity<>(seriesService.search(fieldName, searchContent, sort, sortField, page, limit), HttpStatus.OK);
    }
    @GetMapping("/totalSeries/{username}")
    public ResponseEntity<?> getTotalPost(@PathVariable String username){
        return new ResponseEntity<>(seriesService.countSeriesCreateby(username),HttpStatus.OK);
    }
    @PutMapping("/updateScore")
    public ResponseEntity<?> updateScore( @RequestParam Integer id,@RequestParam int score ){
        return  new ResponseEntity<>(seriesService.upDateScore(id,score),HttpStatus.OK);
    }
}
