package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.SubCommentDto;
import com.caykhe.itforum.models.CommentDetails;
import com.caykhe.itforum.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{targetId}/{type}/add")
    public ResponseEntity<?> addSubcomment(@PathVariable Integer targetId, @PathVariable boolean type,
                                           @Valid @RequestBody SubCommentDto subCommentDto) {
        if (subCommentDto == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        CommentDetails subCommentAggregate = commentService.addSubComment(targetId, type, subCommentDto);
        return new ResponseEntity<>(subCommentAggregate, HttpStatus.CREATED);
    }

    @DeleteMapping("/{targetId}/{type}/{subId}/delete")
    public ResponseEntity<?> addSubcomment(@PathVariable Integer targetId,@PathVariable Integer subId, @PathVariable boolean type) {

        boolean result = commentService.removeSubComment(targetId, subId, type);
        if(result)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new ApiException("Comment không tồn tại", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{targetId}/{type}/{subId}/update")
    public ResponseEntity<?> updateSubComment(@PathVariable Integer targetId, @PathVariable Integer subId,
                                              @PathVariable boolean type, @RequestBody SubCommentDto subCommentDto){
        if (subCommentDto == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        CommentDetails subCommentAggregate = commentService.updateSubComment(targetId, type, subId, subCommentDto);
        return new ResponseEntity<>(subCommentAggregate, HttpStatus.OK);
    }

    @GetMapping("/{targetId}/{type}/get")
    public ResponseEntity<?> getComment(@PathVariable Integer targetId, @PathVariable boolean type,
                                        @RequestParam(required = false, name = "subId") Integer subId){

        List<CommentDetails> subCommentAggregates = commentService.getComments(targetId, type, subId);
        return new ResponseEntity<>(subCommentAggregates, HttpStatus.OK);
    }
}
