package com.edu.hcmute.repository;

import com.edu.hcmute.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplyRepository extends JpaRepository<JobApplication, Long> {
}
