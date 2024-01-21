package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.Field;
import com.edu.hcmute.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
    List<Major> findAllByStatus(Status status);

    Optional<Major> findByName(String name);
}
