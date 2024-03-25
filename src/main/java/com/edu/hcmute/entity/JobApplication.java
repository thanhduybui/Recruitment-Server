package com.edu.hcmute.entity;


import com.edu.hcmute.constant.JobApplyStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "job_application")
@NoArgsConstructor
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobApplyStatus jobApplyStatus;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CV cv;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
}
