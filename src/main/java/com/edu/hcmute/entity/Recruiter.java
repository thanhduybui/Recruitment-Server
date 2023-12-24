package com.edu.hcmute.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Recruiter extends AppUser{
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
