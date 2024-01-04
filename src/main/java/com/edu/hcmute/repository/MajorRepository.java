package com.edu.hcmute.repository;

import com.edu.hcmute.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
}
