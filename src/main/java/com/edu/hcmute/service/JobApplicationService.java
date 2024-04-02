package com.edu.hcmute.service;

import com.edu.hcmute.constant.JobApplicationStatus;
import com.edu.hcmute.dto.JobApplicationRequestBody;

import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.entity.JobApplication;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.JobApplicationMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.JobApplicationRepository;

import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationService {

    private static final String CREATE_JOB_APPLICATION_SUCCESS = "Nộp đơn ứng tuyển thành công";
    private static final String CREATE_JOB_APPLICATION_FAILURE = "Nộp đơn ứng tuyển thất bại";
    private static final String USER_APPLIED_JOB_APPLICATION = "Ứng viên đã ứng tuyển công việc này";
    private static final String GET_ALL_SUCCESS = "Lấy thông tin đơn ứng tuyển thành công";
    private static final String GET_BY_USER_SUCCESS = "Lấy đơn ứng tuyển của ứng viên thành công";



    private final JobApplicationMapper jobApplicationMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final AppUserRepository appUserRepository;


    public ServiceResponse createJobApplication(JobApplicationRequestBody jobApplicationRequestBody) {
        try {
            JobApplication newJobApplication = jobApplicationMapper.jobApplicationRequestBodyToJobApplication(jobApplicationRequestBody);
            List<JobApplication> applications = getByUser(jobApplicationRequestBody.getUserId());

            boolean check = false;

            for(JobApplication item : applications) {
                if (Objects.equals(item.getJob().getId(), jobApplicationRequestBody.getJobId())) {
                    check = true;
                    break;
                }
            }

            if(check) {

                System.out.println("Why not");

                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.CREATED)
                        .message(USER_APPLIED_JOB_APPLICATION)
                        .build();


            }
            else {
                newJobApplication.setApplyStatus(JobApplicationStatus.APPLIED);

                jobApplicationRepository.save(newJobApplication);

                return ServiceResponse.builder()
                        .status(ResponseDataStatus.SUCCESS)
                        .statusCode(HttpStatus.CREATED)
                        .message(CREATE_JOB_APPLICATION_SUCCESS)
                        .build();
            }

        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(CREATE_JOB_APPLICATION_FAILURE);
        }
    }
    public Boolean checkAppliedJobApplication(Long jobId , String email) {
//        List<JobApplication> jobApplication = this.jobApplicationRepository.findAll();
//        this.jobApplicationRepository.flush();
//        System.out.println(jobApplication);
//
//        for (JobApplication item : jobApplication) {
//            if(Objects.equals(item.getAppUser().getId(), appUser.getId())) {
//                if(Objects.equals(item.getJob().getId(), job.getId())) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    public ServiceResponse getAll() {
        try {
            List<JobApplication> listJobApplication = jobApplicationRepository.findAll();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_SUCCESS)
                    .data(Map.of("job_applications",listJobApplication)).build();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(CREATE_JOB_APPLICATION_FAILURE);
        }
    }

    public List<JobApplication> getByUser(Long userId) {
        try {
            return jobApplicationRepository.findByAppUserId(userId);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

//    public ServiceResponse getAllJobApplicationByJob(Integer page, Integer size,Long jobId) {
//        Instant conditionRenderTime = Instant.now().minusSeconds(24*7*60*60);
//
//    }
}
