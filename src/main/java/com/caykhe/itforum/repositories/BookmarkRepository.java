package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Bookmark;
import com.caykhe.itforum.models.Post;
import com.caykhe.itforum.models.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    Optional<Bookmark> findByUsernameUsername(String username);
    Optional<Bookmark> findByUsername(@NotNull User username);

}
