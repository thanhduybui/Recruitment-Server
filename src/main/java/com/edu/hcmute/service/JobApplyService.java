package com.edu.hcmute.service;


import com.edu.hcmute.dto.JobApplyDTO;
import com.edu.hcmute.entity.JobApplication;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.JobApplyMapper;
import com.edu.hcmute.repository.JobApplyRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobApplyService {
    private final JobApplyRepository jobApplyRepository;
    private final JobApplyMapper jobApplyMapper;
    private static final String CREATE_JOB_APPLICATION_SUCCESS = "Tạo đơn ứng tuyển thành công";
    private static final String CREATE_JOB_APPLICATION_FAIL = "Tạo đơn ứng tuyển thất bại";

    public ServiceResponse create(JobApplyDTO jobApplyDTO) {
        try {
            JobApplication jobApplication = jobApplyMapper.jobApplyDTOToJobApply(jobApplyDTO);
            jobApplyRepository.save(jobApplication);
            return ServiceResponse.builder().statusCode(HttpStatus.CREATED)
                    .status(ResponseDataStatus.SUCCESS)
                    .message(CREATE_JOB_APPLICATION_SUCCESS)
                    .build();
        }catch (Exception e) {
            throw new UndefinedException(CREATE_JOB_APPLICATION_FAIL);
        }

    }
}
