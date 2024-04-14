package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Role;
import com.edu.hcmute.entity.AppUser;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    Page<AppUser> findAllByRole(Role role, Pageable pageable);

    @Query("SELECT u FROM AppUser u WHERE u.email LIKE %:email%")
    List<AppUser> findByEmailLike(@Param("email") String email);
}

