package com.edu.hcmute.repository;

import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Canditdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CandidateRepository extends JpaRepository<Canditdate, Long>{
    Optional<Canditdate> findByEmail(String email);

}
