package com.edu.hcmute.service;

import com.edu.hcmute.constant.JobApplicationStatus;
import com.edu.hcmute.dto.JobApplicationRequestBody;

import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.entity.JobApplication;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.JobApplicationMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CvRepository;
import com.edu.hcmute.repository.JobApplicationRepository;

import com.edu.hcmute.repository.JobRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    private static final String CV_NOT_FOUND = "Không tìm thấy CV";

    private static final String JOB_NOT_FOUND = "Không tìm thấy công việc";


    private final JobApplicationMapper jobApplicationMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final AppUserRepository appUserRepository;
    private final CvRepository cvRepository;
    private final JobRepository jobRepository;


    public AppUser getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        return appUser.orElse(null);
    }


    public ServiceResponse createJobApplication(JobApplicationRequestBody jobApplicationRequestBody) {

            List<JobApplication> applications;
            JobApplication newJobApplication = jobApplicationMapper.jobApplicationRequestBodyToJobApplication(jobApplicationRequestBody);

           cvRepository.findById(Long.valueOf(jobApplicationRequestBody.getCvId())).orElseThrow(() -> new ResourceNotFoundException(CV_NOT_FOUND));

           jobRepository.findById(jobApplicationRequestBody.getJobId()).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

            if (getUser() != null) {
                newJobApplication.setAppUser(getUser());

                applications = jobApplicationRepository.findByAppUserId(getUser().getId());
                boolean check = false;
                for (JobApplication item : applications) {
                    if (Objects.equals(item.getJob().getId(), jobApplicationRequestBody.getJobId())) {
                        check = true;
                        break;
                    }
                }

                if (check) {
                    return ServiceResponse.builder()
                            .status(ResponseDataStatus.ERROR)
                            .statusCode(HttpStatus.CREATED)
                            .message(USER_APPLIED_JOB_APPLICATION)
                            .build();

                }

            }

            newJobApplication.setApplyStatus(JobApplicationStatus.APPLIED);

            jobApplicationRepository.save(newJobApplication);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED)
                    .message(CREATE_JOB_APPLICATION_SUCCESS)
                    .build();

    }


    public ServiceResponse getAll() {
        try {
            List<JobApplication> listJobApplication = jobApplicationRepository.findAll();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_SUCCESS)
                    .data(Map.of("job_applications", listJobApplication)).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(CREATE_JOB_APPLICATION_FAILURE);
        }
    }

    public List<JobApplication> getByUser(Long userId) {
        try {
            return jobApplicationRepository.findByAppUserId(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
