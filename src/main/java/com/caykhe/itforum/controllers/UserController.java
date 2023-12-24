package com.caykhe.itforum.controllers;

import com.caykhe.itforum.dtos.ResultCount;
import com.caykhe.itforum.dtos.UserStats;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    final private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        Optional<List<User>> userListOptional = userService.getAllUser();

        if (userListOptional.isPresent()) {
            List<User> userList = userListOptional.get();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping("/{follower}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable String follower, Integer page, Integer size) {
        ResultCount<UserStats> followings = userService.getFollowings(follower, page, size);
        return new ResponseEntity<>(followings, HttpStatus.OK);
    }
    
    @GetMapping("/{followed}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String followed, Integer page, Integer size) {
        ResultCount<UserStats> followers = userService.getFollowers(followed, page, size);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
    
}
