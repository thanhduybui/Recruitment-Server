package com.edu.hcmute.repository;

import com.edu.hcmute.constant.JobApplyStatus;
import com.edu.hcmute.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    @Query("SELECT ja FROM JobApplication ja WHERE ja.appUser.id = :userID ORDER BY ja.createdAt DESC")
    List<JobApplication> findByAppUserId(@Param("userID") Long userID);
    Page<JobApplication> findAllByJobId(Long jobId, Pageable pageable);

    Page<JobApplication> findAllByJobIdAndStatus(Long job_id, JobApplyStatus status, Pageable pageable);
}
