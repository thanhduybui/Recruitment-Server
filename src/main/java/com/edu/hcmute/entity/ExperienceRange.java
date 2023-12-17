package com.edu.hcmute.entity;


import com.edu.hcmute.constant.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@Builder
@Entity
@Table(name = "experience_range")
public class ExperienceRange {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column (name = "name", unique = true)
    private String name;
    @Column (name = "description")
    private String description;
    @Column (name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
}
