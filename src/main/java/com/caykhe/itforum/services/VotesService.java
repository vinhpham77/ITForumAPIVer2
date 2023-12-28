package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.VoteRequest;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.models.Vote;
import com.caykhe.itforum.models.VoteId;
import com.caykhe.itforum.repositories.VoteRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotesService {
    final VoteRepository voteRepository;
    final UserService userService;
    final EntityManager entityManager;
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

//    public Optional<Vote> getVoteById(VoteRequest voteRequest) {
//
//        VoteId voteId= new VoteId();
//        voteId.setTargetId(voteRequest.getTargetId());
//        voteId.setUsername(voteRequest);
//        voteId.setType(voteRequest.getType());
//        return voteRepository.findById(voteId);
//    }
    @Transactional
    public Vote createVote(VoteRequest voteRequest) {
        var user = userService.getUserByUsername(voteRequest.getUsername());
        User managedUser = entityManager.merge(user);
        Vote vote = Vote.builder()
                .targetId(voteRequest.getTargetId())
                .type(voteRequest.getType())
                .user(managedUser)
                .updatedAt(Instant.now()).build();
        return voteRepository.save(vote);
    }



    public void unVote(VoteRequest voteRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VoteId voteId= new VoteId();
        voteId.setTargetId(voteRequest.getTargetId());
        voteId.setUser(user);
        voteId.setType(voteRequest.getType());
        voteRepository.deleteById(voteId);
    }

    public Optional<Vote> hasVoted(VoteRequest voteRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VoteId voteId= new VoteId();
        voteId.setTargetId(voteRequest.getTargetId());
        voteId.setUser(user);
        voteId.setType(voteRequest.getType());
        return voteRepository.findById(voteId);
    }





}
