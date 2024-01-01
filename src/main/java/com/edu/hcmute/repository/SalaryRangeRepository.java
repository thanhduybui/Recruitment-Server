package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.Position;
import com.edu.hcmute.entity.SalaryRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SalaryRangeRepository extends JpaRepository<SalaryRange, Integer> {
    List<SalaryRange> findAllByStatus(Status status);
}
