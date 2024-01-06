package com.edu.hcmute.service;


import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.JobMapper;
import com.edu.hcmute.repository.JobRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private static final String CREATE_JOB_SUCCESS = "Tạo công việc thành công";
    private static final String CREATE_JOB_FAIL = "Tạo công việc thất bại";
    private static final String UPDATE_JOB_SUCCESS = "Cập nhật công việc thành công";
    private static final String UPDATE_JOB_FAIL = "Cập nhật công việc thất bại";
    private static final String JOB_NOT_FOUND = "Không tìm thấy công việc";
    private static final String GET_JOB_SUCCESS = "Lấy thông tin công việc thành công";
    private static final String GET_JOB_FAIL = "Lấy thông tin công việc thất bại";

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    public ServiceResponse create(JobRequestBody jobRequestBody) {
        try {
            Job newJob = jobMapper.jobRequestBodyToJob(jobRequestBody);
            jobRepository.save(newJob);
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED)
                    .message(CREATE_JOB_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(CREATE_JOB_FAIL);
        }
    }

    public ServiceResponse update(Long id, JobRequestBody jobRequestBody) {
        try{
            Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

            jobMapper.updateJobFromJobRequestBody(jobRequestBody, job);

            this.jobRepository.save(job);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(UPDATE_JOB_SUCCESS)
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new UndefinedException(UPDATE_JOB_FAIL);
        }
    }

    public ServiceResponse getOne(Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
            JobDTO jobDTO = jobMapper.jobToJobDTO(job);
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_JOB_SUCCESS)
                    .data(Map.of("job", jobDTO))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(GET_JOB_FAIL);
        }
    }
}
