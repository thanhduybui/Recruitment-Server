package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.WorkMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WorkModeRepository extends JpaRepository<WorkMode, Integer> {

    List<WorkMode> findAllByStatus(Status status);
}
