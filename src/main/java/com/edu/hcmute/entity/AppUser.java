package com.edu.hcmute.entity;


import com.edu.hcmute.constant.Gender;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "status")
    private Status status;

    @Lob
    @Column(name = "avatar_url")
    private String avatar;

    @Column(name = "warning")
    private Boolean warning;

    @Column(name = "work_location")
    private Integer workLocation;

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
}
