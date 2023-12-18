package com.edu.hcmute.entity;


import com.edu.hcmute.constant.Role;
import com.edu.hcmute.constant.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@Entity
@Table(name = "job")
@NoArgsConstructor
public class Job {
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
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "deadline")
    private Instant deadline;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "created_by")
    private String createdBy;
}
