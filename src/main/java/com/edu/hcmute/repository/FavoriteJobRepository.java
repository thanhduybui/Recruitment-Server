package com.edu.hcmute.repository;

import com.edu.hcmute.entity.FavoriteJob;
import com.edu.hcmute.mapper.FavoriteJobMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteJobRepository extends JpaRepository<FavoriteJob, Integer> {

    List<FavoriteJob> findFavoriteJobByUserIdAndJobId(Long userId, Long JobId);
}
