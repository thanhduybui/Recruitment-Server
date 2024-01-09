package com.edu.hcmute.repository;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAllByCompany(Company company, Pageable pageable);

    Page<Job> findAllByCompanyAndStatusAndDeadlineAfter(Company company, Status status, Instant instant, Pageable pageable);

    Page<Job> findAllByCompanyAndDeadlineBefore(Company company, Instant instant, Pageable pageable);

    Page<Job> findAllByCompanyAndStatusAndIsHot(Company company, Status status, boolean b, Pageable pageable);

    Page<Job> findAllByStatus(Status status, Pageable pageable);
}
