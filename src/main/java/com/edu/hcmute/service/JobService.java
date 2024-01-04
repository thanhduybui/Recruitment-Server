package com.edu.hcmute.service;


import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.mapper.JobMapper;
import com.edu.hcmute.repository.JobRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private static final String CREATE_JOB_SUCCESS = "Tạo công việc thành công";
    private static final String CREATE_JOB_FAIL = "Tạo công việc thất bại";

    public ServiceResponse create(JobRequestBody jobRequestBody){
       try{
           Job newJob = jobMapper.jobRequestBodyToJob(jobRequestBody);
           jobRepository.save(newJob);
           return ServiceResponse.builder()
                   .status(ResponseDataStatus.SUCCESS)
                   .statusCode(HttpStatus.CREATED)
                   .message(CREATE_JOB_SUCCESS)
                   .build();
       }catch (Exception e){
           log.error(e.getMessage());
          return ServiceResponse.builder()
                  .status(ResponseDataStatus.ERROR)
                  .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                  .message(CREATE_JOB_FAIL)
                  .build();
       }
    }

}
