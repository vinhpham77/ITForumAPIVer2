package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);
}