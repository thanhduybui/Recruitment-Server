package com.edu.hcmute.repository;


import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CV, Integer> {
    List<CV> findAllByCandidate(AppUser user);
}
