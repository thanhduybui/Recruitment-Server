package com.edu.hcmute.repository;

import com.edu.hcmute.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    List<JobApplication> findByAppUserId(Long userID);
    Page<JobApplication> findAllByJobId(Long jobId, Pageable pageable);
}
