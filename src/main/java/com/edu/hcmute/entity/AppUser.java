package com.edu.hcmute.entity;


import com.edu.hcmute.constant.Gender;
import com.edu.hcmute.constant.LoginType;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phoneNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Lob
    @Column(name = "avatar_url")
    private String avatar;

    @Column(name = "warning")
    private Boolean warning;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "provider")
    private String provider;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location workLocation;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;


    @ManyToOne
    @JoinColumn(name = "exp_range_id")
    private ExperienceRange experienceRange;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite_job",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> favoriteJobs;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private Company company;


    @OneToMany(mappedBy = "candidate", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<CV> cvList;

    @OneToMany(mappedBy = "receiver", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Notification> notifications;
}
