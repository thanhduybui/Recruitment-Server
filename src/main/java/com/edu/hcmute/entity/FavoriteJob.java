package com.edu.hcmute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "favorite_job")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FavoriteJob {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
}
