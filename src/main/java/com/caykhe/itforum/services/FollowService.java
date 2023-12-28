package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.BookmarkPostRequest;
import com.caykhe.itforum.models.BookmarkPost;
import com.caykhe.itforum.models.Follow;
import com.caykhe.itforum.models.Notification;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.FollowRepository;
import com.caykhe.itforum.repositories.NotificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    final FollowRepository followRepository;
    final UserService userService;
    final EntityManager entityManager;

    public Optional<Follow> getFollow(String followed) {
        User followerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User followedUser = userService.getUserByUsername(followed);
        
        return followRepository.findByFollowerAndFollowed(followerUser, followedUser);
    }
    @Autowired
    private NotificationRepository notificationRepository;
    @Transactional
    public Follow follow(String followed) {
        User follower = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (follower.getUsername().equals(followed)) {
            throw new ApiException("Không thể theo dõi chính mình", HttpStatus.BAD_REQUEST);
        } else if (getFollow(followed).isPresent()) {
            throw new ApiException("Đã theo dõi trước đó", HttpStatus.BAD_REQUEST);
        }

        User managedFollower = entityManager.merge(follower);
        var followedUser = userService.getUserByUsername(followed);
        User managedFollowed = entityManager.merge(followedUser);
        
        
        Follow follow = Follow.builder()
                .follower(managedFollower)
                .followed(managedFollowed)
                .build();
        System.out.println(follow.toString());
        try {
            // Tạo và lưu thông báo
            Notification notification = new Notification();
            notification.setUsername(follower.getUsername()); // Sửa lại thành username
            notification.setContent("@" + follower + " đã theo dõi : " + followedUser);
            notification.setCreatedAt(Instant.now());
            notification.setRead(false);
            notification.setType("follow");
            notification.setTargetId(followedUser.getId());
            notificationRepository.save(notification);
            return followRepository.save(follow);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ApiException("Có lỗi xảy ra khi theo dõi. Vui lòng thử lại!", HttpStatus.BAD_REQUEST);
        }
    }

    public void unfollow(String followed) {
        var follow = getFollow(followed);
        
        if (follow.isEmpty()) {
            throw new ApiException("Chưa theo dõi", HttpStatus.BAD_REQUEST);
        }

        followRepository.delete(follow.get());
    }

    public List<String> getFollowedByFollower() {
        User followerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Follow> follows = followRepository.findByFollower(followerUser)
                .orElseThrow(() -> new ApiException("Tài khoản không tồn tại", HttpStatus.NOT_FOUND));
        return follows.stream().map(follow -> follow.getFollowed().getUsername()).collect(Collectors.toList());
    }
}
