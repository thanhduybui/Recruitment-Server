package com.edu.hcmute.entity;


import com.edu.hcmute.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    @Lob
    private String image;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "scale")
    private String scale;
    @Column(name = "branch")
    private String address;
    @Column(name="email")
    private String email;
    @Column(name="phone")
    private String phone;
    @Column(name="web_url")
    private String webUrl;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name ="business_license")
    private String businessLicense;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY)
    private AppUser recruiter;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Job> jobs;
}
