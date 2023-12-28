package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.VoteRequest;
import com.caykhe.itforum.models.Vote;
import com.caykhe.itforum.models.VoteId;
import com.caykhe.itforum.services.VotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController {
    final private VotesService votesService;
    @GetMapping
    public List<Vote> getAllVotes() {
        return votesService.getAllVotes();
    }



    @PostMapping("/createVote")
    public ResponseEntity<?> createVote(@RequestBody VoteRequest voteRequest) {
        return new ResponseEntity<>(votesService.createVote(voteRequest), HttpStatus.OK) ;
    }
    @PostMapping("/unVote")
    public ResponseEntity<?> unVote(@RequestBody VoteRequest voteRequest) {
        votesService.unVote(voteRequest);
        return new ResponseEntity<>("Xóa thành công",HttpStatus.OK) ;
    }

    @PostMapping("/checkVote")
    public ResponseEntity<?> checkVote(@RequestBody VoteRequest voteRequest) {
        votesService.hasVoted(voteRequest);
        return new ResponseEntity<>("Xóa thành công",HttpStatus.OK) ;
    }
}
