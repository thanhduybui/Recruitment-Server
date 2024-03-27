package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByAppUserId(Long userID);
}
