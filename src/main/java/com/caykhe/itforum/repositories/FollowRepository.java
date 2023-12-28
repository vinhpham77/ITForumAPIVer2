package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Follow;
import com.caykhe.itforum.models.FollowId;
import com.caykhe.itforum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    Page<Follow> findAllByFollowed(User followed, Pageable pageable);

    Page<Follow> findAllByFollower(User follower, Pageable pageable);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    Optional<List<Follow>> findByFollower(User follower);
    List<Follow> findByFollowed(User follower);

    int countByFollowed(User user);

    int countByFollower(User user);
}
