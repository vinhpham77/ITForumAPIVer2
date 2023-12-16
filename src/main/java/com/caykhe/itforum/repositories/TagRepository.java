package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}