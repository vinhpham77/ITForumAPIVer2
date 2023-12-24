package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.dtos.UserStats;
import com.caykhe.itforum.models.Follow;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.FollowRepository;
import com.caykhe.itforum.repositories.PostRepository;
import com.caykhe.itforum.repositories.SeriesRepository;
import com.caykhe.itforum.repositories.UserRepository;
import com.caykhe.itforum.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final SeriesRepository seriesRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng @" + username));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng @" + username, HttpStatus.NOT_FOUND));
    }

    public void deleteByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent())
            userRepository.delete(userOptional.get());
        else
            throw new ApiException("Không tìm thấy user cần xóa", HttpStatus.NOT_FOUND);

    }

    public Optional<List<User>> getAllUser() {
        return Optional.of(userRepository.findAll());
    }

    public ResultCount<UserStats> getFollowings(String followed, Integer page, Integer size) {
        User followedUser = getUserByUsername(followed);
        Pageable pageable = PaginationUtils.getPageable(page, size, "followed");
        Page<Follow> followersPage = followRepository.findAllByFollowed(followedUser, pageable);
        
        return countAndAddStates(followersPage);
    }
    
    public ResultCount<UserStats> getFollowers(String follower, Integer page, Integer size) {
        User followerUser = getUserByUsername(follower);
        Pageable pageable = PaginationUtils.getPageable(page, size, "follower");
        Page<Follow> followedsPage = followRepository.findAllByFollower(followerUser, pageable);
        
        return countAndAddStates(followedsPage);
    }
    
    private ResultCount<UserStats> countAndAddStates(Page<Follow> followePage) {
        long count = followePage.getTotalElements();
        var follows = followePage.stream().map(Follow::getFollowed);

        List<UserStats> userStats = follows.map(user -> {
            int postCount = postRepository.countByCreatedBy(user);
            int seriesCount = seriesRepository.countByCreatedBy(user);
            int followerCount = followRepository.countByFollowed(user);

            return UserStats.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .displayName(user.getDisplayName())
                    .role(user.getRole())
                    .postCount(postCount)
                    .seriesCount(seriesCount)
                    .followerCount(followerCount)
                    .build();
        }).toList();
        
        return new ResultCount<>(userStats, count);
    }
}
