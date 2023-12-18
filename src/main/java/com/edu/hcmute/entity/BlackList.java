package com.edu.hcmute.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@Table(name = "black_list")
@Entity
@Builder
public class BlackList {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "reason")
    @Lob
    private String reason;
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
