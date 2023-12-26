package com.caykhe.itforum.dtos;

import org.springframework.data.domain.Pageable;

public record UserLatestPageable(String requester, Pageable pageable) { }