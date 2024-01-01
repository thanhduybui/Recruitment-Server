package com.edu.hcmute.repository;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.ExperienceRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRangeRepository extends JpaRepository<ExperienceRange, Integer> {
    List<ExperienceRange> findAllByStatus(Status status);
}
