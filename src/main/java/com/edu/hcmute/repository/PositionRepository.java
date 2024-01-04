package com.edu.hcmute.repository;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    List<Position> findAllByStatus(Status status);
    Optional<Position> findByName(String name);
}
