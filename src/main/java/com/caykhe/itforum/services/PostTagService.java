package com.caykhe.itforum.services;

import com.caykhe.itforum.repositories.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostTagService {
    private final PostTagRepository postTagRepository;
    
    
}
