package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Integer> {



    boolean existsByPostIdAndUsername(String postId, String username);
   Optional <Vote> findByPostIdAndUsername(String postId, String username);

   Vote findByPostId(String postId);
}
