package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.User;
import com.caykhe.itforum.models.Vote;
import com.caykhe.itforum.models.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
}
