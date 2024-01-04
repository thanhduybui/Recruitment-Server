package com.edu.hcmute.entity;


import com.edu.hcmute.constant.Role;
import com.edu.hcmute.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "job")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Job implements Serializable {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    @Lob
    private String description;
    @Column(name = "requirement")
    @Lob

    private String requirement;
    @Column(name = "benefit")
    @Lob
    private String benefit;

    @Column(name = "slots")
    private Integer slots;

    @Column(name = "view")
    private Integer view;

    @Column(name = "apply_number")
    private Integer applyNumber;

    @Column(name = "like_number")
    private Integer likeNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "deadline")
    private Instant deadline;

    @Column(name = "work_location")
    @Lob
    private String workLocation;

    @Column(name = "work_time")
    @Lob
    private String workTime;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "is_hot")
    private Boolean isHot;

    @ManyToOne
    @JoinColumn(name = "salary_range_id")
    private SalaryRange salaryRange;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "work_mode_id")
    private WorkMode workMode;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne
    @JoinColumn(name = "experience_range_id")
    private ExperienceRange experienceRange;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;
}
