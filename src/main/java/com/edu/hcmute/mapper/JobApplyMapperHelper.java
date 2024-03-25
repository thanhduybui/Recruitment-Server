package com.edu.hcmute.mapper;


import com.edu.hcmute.entity.CV;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.repository.CvRepository;
import com.edu.hcmute.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApplyMapperHelper {
    private final JobRepository jobRepository;
    private final CvRepository cvRepository;

    Job jobIdToJob(Long id) {
        return jobRepository.findById(id).orElse(null);
    }
    CV cvIdToCv(Long id) {
        return cvRepository.findById(id).orElse(null);
    }

}
