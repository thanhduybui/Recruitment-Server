package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.CandidateJobDTO;
import com.edu.hcmute.dto.CvDTO;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.repository.CvRepository;
import com.edu.hcmute.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApplicationMapperHelper {
    private final JobMapper jobMapper;
    private final CVMapper cvMapper;

   CandidateJobDTO jobToJobDTO(Job job) {
       if (job == null) {
                return null;
            }
       return jobMapper.jobToCandidateJobDTO(job);
    }
    CvDTO cvToCvDTO(CV cv) {
        if (cv == null) {
            return null;
        }
        return cvMapper.CVtoCvDTO(cv);
    }

}
