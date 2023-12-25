package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.VoteRequest;
import com.caykhe.itforum.models.Vote;
import com.caykhe.itforum.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Optional<Vote> getVoteById(Integer id) {
        return voteRepository.findById(id);
    }
    public Vote getVotePostId(String postId) {
        return voteRepository.findByPostId(postId);
    }
//    public Vote createVote(VoteRequest voteRequest) {
//        Vote vote = Vote.builder()
//                .postId(voteRequest.getPostId())
//                .type(voteRequest.getType())
//                .username(voteRequest.getUsername())
//                .updatedAt(voteRequest.getUpdatedAt()).build();
//
//        return voteRepository.save(vote);
//    }

//    public Vote updateVote(Integer id, VoteRequest voteRequest) {
//        Optional<Vote> voteOptional = voteRepository.findById(id);
//        if (voteOptional.isPresent()) {
//            Vote vote = voteOptional.get();
//            vote.setType(voteRequest.getType());
//            vote.setUpdatedAt(voteRequest.getUpdatedAt());
//            return voteRepository.save(vote);
//        } else {
//            throw new ApiException("Lỗi không thấy Vote cần tìm", HttpStatus.NOT_FOUND);
//        }
//    }


    public void unVote(Integer voteId) {
        voteRepository.deleteById(voteId);
    }

    public Optional<Vote> hasVoted(String postId, String username) {
        return voteRepository.findByPostIdAndUsername(postId, username);
    }
    public Vote getVote(String postId){
        return voteRepository.findByPostId(postId);
    }



}
