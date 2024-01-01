package com.edu.hcmute.repository;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
    List<Field> findAllByStatus(Status status);
}
