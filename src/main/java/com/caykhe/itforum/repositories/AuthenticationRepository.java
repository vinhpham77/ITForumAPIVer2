package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Authentication;
import com.caykhe.itforum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
    Optional<Authentication> findByUsername(User user);
    void deleteByUsername(User user);

}
