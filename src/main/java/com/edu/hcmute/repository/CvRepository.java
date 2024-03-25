package com.edu.hcmute.repository;

import com.edu.hcmute.entity.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvRepository extends JpaRepository<CV, Long>{

}
