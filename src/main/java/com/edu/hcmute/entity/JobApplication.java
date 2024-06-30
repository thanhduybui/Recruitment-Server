package com.edu.hcmute.entity;


import com.edu.hcmute.constant.JobApplyStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Data
@Table(name = "job_application")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobApplication {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CV cv;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;


    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "status" , nullable = false)
    @Enumerated(EnumType.STRING)
    private JobApplyStatus status;
}
