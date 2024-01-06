package com.edu.hcmute.repository;


import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findAllByCompany(Company company, Pageable pageable);
}
