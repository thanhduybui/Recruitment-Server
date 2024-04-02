package com.edu.hcmute.repository;

import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.FavoriteJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteJobRepository extends JpaRepository<FavoriteJob, Integer> {

    List<FavoriteJob> findFavoriteJobByUserIdAndJobId(Long userId, Long JobId);

    Page<FavoriteJob> findAllByUser(AppUser user, Pageable pageable);

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    void deleteByJobId(Long id);
}
