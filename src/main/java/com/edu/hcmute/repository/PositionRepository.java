package com.edu.hcmute.repository;

import com.edu.hcmute.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
}
