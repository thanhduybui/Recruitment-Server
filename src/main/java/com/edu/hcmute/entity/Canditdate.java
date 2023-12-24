package com.edu.hcmute.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Canditdate extends AppUser{
    @Column(name = "work_location")
    private Integer workLocation;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne
    @JoinColumn(name = "salary_range_id")
    private SalaryRange salaryRange;

    @ManyToOne
    @JoinColumn(name = "exp_range_id")
    private ExperienceRange experienceRange;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite_job",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> favoriteJobs;
}
