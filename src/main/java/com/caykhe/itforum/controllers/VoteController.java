package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.VoteRequest;
import com.caykhe.itforum.models.Vote;
import com.caykhe.itforum.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {
    final VoteService votesService;

    @GetMapping("/checkVote")
    public ResponseEntity<?> checkVote(  @RequestParam  String id,
                                         @RequestParam String username) {
        Optional<Vote> voted = votesService.hasVoted(id, username);
        if (voted.isPresent())
            return new ResponseEntity<Vote>(voted.get(), HttpStatus.OK);
       else  return ResponseEntity.noContent().build();
    }
    @GetMapping("/byPostId")
    public ResponseEntity<?>getOneVote(@RequestParam String postId){
        Vote vote =votesService.getVotePostId(postId);
        return new  ResponseEntity<>(vote,HttpStatus.OK);
    }
    @GetMapping("/checkListVote")
    public ResponseEntity<?> checkVote( ) {
        List<Vote> voted = votesService.getAllVotes();
         return new ResponseEntity<>(voted, HttpStatus.OK);
    }
//    @PostMapping("/createVote")
//    public ResponseEntity<?> createVote(@RequestBody VoteRequest voteRequest){
//        return new ResponseEntity<>(votesService.createVote(voteRequest),HttpStatus.OK);
//    }
//    @PostMapping ("/updateVote/{id}")
//    public ResponseEntity<?> updateVote(@PathVariable Integer id, @RequestBody VoteRequest voteRequest){
//        return new ResponseEntity<>(votesService.updateVote(id, voteRequest),HttpStatus.OK);
//    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getByUsername(@PathVariable Integer id) {

        Optional<Vote> userOptional = votesService.getVoteById(id);

            return new ResponseEntity<>(userOptional, HttpStatus.OK);

    }
    @DeleteMapping("/deleteVote/{id}")
    public ResponseEntity<?> deleteVote(@PathVariable Integer id){
        votesService.unVote(id);
        return new ResponseEntity<>("Xóa thành công",HttpStatus.OK);
    }

}
