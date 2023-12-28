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
import org.springframework.dao.DataAccessException;
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

    public Vote voteById(Integer targetId,Boolean targetType) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return voteRepository.findByTargetIdAndTargetTypeAndUser(targetId,targetType,user)
                .orElseThrow(() -> new ApiException("Vote không tìm thấy", HttpStatus.NOT_FOUND));
    }
    @Transactional
    public Vote createVote(VoteRequest voteRequest) {
        var user = userService.getUserByUsername(voteRequest.getUsername());
        User managedUser = entityManager.merge(user);
        Vote vote = Vote.builder()
                .targetId(voteRequest.getTargetId())
                .voteType(voteRequest.getVoteType())
                .targetType(voteRequest.getTargetType())
                .user(managedUser)
                .updatedAt(Instant.now()).build();
        return voteRepository.save(vote);
    }



    public void unVote(Integer targetId, Boolean targetType) {
       Vote vote= voteById(targetId,targetType);
       voteRepository.delete(vote);
    }
    public Vote hasVoted(Integer targetId,Boolean targetType) {

        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals("anonymousUser")){
            throw new ApiException("Chưa đăng nhập", HttpStatus.NOT_FOUND);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Vote> vote= voteRepository.findByTargetIdAndTargetTypeAndUser(targetId,targetType,user);
        return vote.orElse(null);

    }
}
