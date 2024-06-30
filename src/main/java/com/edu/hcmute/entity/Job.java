package com.edu.hcmute.entity;

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
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(name = "benefit", columnDefinition = "TEXT")
    private String benefit;

    @Lob
    @Column(name = "requirement", columnDefinition = "TEXT")
    private String requirement;

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

    @Lob
    @Column(name = "work_location", columnDefinition = "TEXT")
    private String workLocation;

    @Column(name = "work_time", columnDefinition = "TEXT")
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

    @Column(name = "salary")
    private Integer salary;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "work_mode_id")
    private WorkMode workMode;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "experience_range_id")
    private ExperienceRange experienceRange;

}
