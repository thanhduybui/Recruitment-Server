package com.edu.hcmute.repository;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAllByCompany(Company company, Pageable pageable);

    Page<Job> findAllByCompanyAndStatusAndDeadlineAfter(Company company, Status status, Instant instant, Pageable pageable);

    Page<Job> findAllByStatus(Status status, Pageable pageable);

    Page<Job> findAllByCompanyAndDeadlineBefore(Company company, Instant instant, Pageable pageable);

    Page<Job> findAllByCompanyAndStatusAndIsHot(Company company, Status status, boolean b, Pageable pageable);


    @Query("SELECT j FROM Job j " +
            "WHERE (:keyword IS NULL OR j.title LIKE %:keyword%) " +
            "AND (:locationId IS NULL OR j.locationId = :locationId) " +
            "AND (:wokeModeId IS NULL OR j.workMode.id = :wokeModeId) " +
            "AND (:fieldId IS NULL OR j.field.id = :fieldId) " +
            "AND (:majorId IS NULL OR j.major.id = :majorId) " +
            "AND (:salaryId IS NULL OR j.salaryRange.id = :salaryId) " +
            "AND (:experienceId IS NULL OR j.experienceRange.Id = :experienceId) " +
            "AND (:positionId IS NULL OR j.position.id = :positionId) " +
            "AND (:isHot IS NULL OR j.isHot = :isHot) " +
            "AND (:date IS NULL OR j.deadline > :date) " +
            "AND  j.status = :status " +
            "ORDER BY j.createdAt DESC")
    Page<Job> findByFilterCriteria(@Param("keyword") String keyword,
                                   @Param("locationId") Integer locationId,
                                   @Param("wokeModeId") Integer wokeModeId,
                                   @Param("fieldId") Integer fieldId,
                                   @Param("majorId") Integer majorId,
                                   @Param("salaryId") Integer salaryId,
                                   @Param("experienceId") Integer experienceId,
                                   @Param("positionId") Integer positionId,
                                   @Param("isHot") Boolean isHot,
                                   @Param("status") Status status,
                                   @Param("date") Instant date,
                                   Pageable pageable);
}
