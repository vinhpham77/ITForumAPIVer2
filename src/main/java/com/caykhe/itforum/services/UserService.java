package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.*;
import com.caykhe.itforum.models.Follow;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.*;
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
    private final PostTagRepository postTagRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng @" + username));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng @" + username, HttpStatus.NOT_FOUND));
    }

    public Optional<List<User>> getAllUser() {
        return Optional.of(userRepository.findAll());
    }

    public ResultCount<UserStats> getFollowings(String follower, Integer page, Integer size) {
        User followerUser = getUserByUsername(follower);
        Pageable pageable = PaginationUtils.getPageable(page - 1, size, "followed");
        Page<Follow> followersPage = followRepository.findAllByFollower(followerUser, pageable);

        return countAndAddStates(followersPage, true);
    }

    public ResultCount<UserStats> getFollowers(String followed, Integer page, Integer size) {
        User followedUser = getUserByUsername(followed);
        Pageable pageable = PaginationUtils.getPageable(page - 1, size, "follower");
        Page<Follow> followedsPage = followRepository.findAllByFollowed(followedUser, pageable);

        return countAndAddStates(followedsPage, false);
    }

    private ResultCount<UserStats> countAndAddStates(Page<Follow> followePage, boolean isFollowing) {
        long count = followePage.getTotalElements();
        var follows = followePage.stream().map(Follow::getFollowed);
        
        if (isFollowing) {
            follows = followePage.stream().map(Follow::getFollowed);
        } else {
            follows = followePage.stream().map(Follow::getFollower);
        }

        List<UserStats> userStats = follows.map(user -> {
            int postCount = postRepository.countByCreatedBy(user);
            int seriesCount = seriesRepository.countByCreatedBy(user);
            int followCount;

            if (isFollowing) {
                followCount = followRepository.countByFollowed(user);
            } else {
                followCount = followRepository.countByFollower(user);
            }

            return UserStats.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .displayName(user.getDisplayName())
                    .role(user.getRole())
                    .postCount(postCount)
                    .seriesCount(seriesCount)
                    .followerCount(followCount)
                    .build();
        }).toList();

        return new ResultCount<>(userStats, count);
    }

    public List<TagCount> getTagCounts(String username) {
        getUserByUsername(username);

        return postTagRepository.countTagsByUsername(username);
    }

    public ProfileStats getProfileStats(String username) {
        User user = getUserByUsername(username);
        int postCount = postRepository.countByCreatedBy(user);
        int questionCount = postTagRepository.countByPost_CreatedByAndTag_Name(user, "HoiDap");
        int seriesCount = seriesRepository.countByCreatedBy(user);
        int followingCount = followRepository.countByFollower(user);
        int followerCount = followRepository.countByFollowed(user);

        return ProfileStats.builder()
                .postCount(postCount)
                .questionCount(questionCount)
                .seriesCount(seriesCount)
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }
}
