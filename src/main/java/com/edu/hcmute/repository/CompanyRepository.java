package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Page<Company> findAllByStatus(Status status, Pageable pageable);
}
