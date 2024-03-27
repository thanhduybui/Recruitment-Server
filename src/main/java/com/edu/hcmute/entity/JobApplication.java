package com.edu.hcmute.entity;


import com.edu.hcmute.constant.JobApplicationStatus;
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
    private Integer id;
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

    @Column(name = "apply_status" , nullable = false)
    @Enumerated(EnumType.STRING)
    private JobApplicationStatus applyStatus;

}
