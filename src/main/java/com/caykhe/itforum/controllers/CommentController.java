package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.SubCommentAggregate;
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

    @PostMapping("/{targetId}/add")
    public ResponseEntity<?> addSubcomment(@PathVariable Integer targetId, @Valid @RequestBody SubCommentDto subCommentDto) {
        if (subCommentDto == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        CommentDetails subCommentAggregate = commentService.addSubComment(targetId, subCommentDto);
        return new ResponseEntity<>(subCommentAggregate, HttpStatus.CREATED);
    }

    @DeleteMapping("/{targetId}/{subId}/delete")
    public ResponseEntity<?> addSubcomment(@PathVariable Integer targetId,@PathVariable Integer subId) {

        boolean result = commentService.removeSubComment(targetId, subId);
        if(result)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new ApiException("Comment không tồn tại", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{targetId}/{subId}/update")
    public ResponseEntity<?> updateSubComment(@PathVariable Integer targetId, @PathVariable Integer subId, @RequestBody SubCommentDto subCommentDto){
        if (subCommentDto == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        CommentDetails subCommentAggregate = commentService.updateSubComment(targetId, subId, subCommentDto);
        return new ResponseEntity<>(subCommentAggregate, HttpStatus.OK);
    }

    @GetMapping("/{targetId}/get")
    public ResponseEntity<?> getComment(@PathVariable Integer targetId,
                                        @RequestParam(required = false, name = "subId") Integer subId){

        List<CommentDetails> subCommentAggregates = commentService.getComments(targetId, subId);
        return new ResponseEntity<>(subCommentAggregates, HttpStatus.OK);
    }
}
