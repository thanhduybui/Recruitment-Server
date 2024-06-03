package com.edu.hcmute.service.job;

import com.edu.hcmute.constant.JobApplyStatus;
import com.edu.hcmute.dto.JobApplicationDTO;
import com.edu.hcmute.dto.JobApplicationRequestBody;

import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.entity.JobApplication;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.JobApplicationMapper;
import com.edu.hcmute.repository.*;

import com.edu.hcmute.response.PagingResponseData;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;

import com.edu.hcmute.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private static final String GET_BY_USER_SUCCESS = "Lấy đơn ứng tuyển của ứng viên thành công";
    private static final String GET_ALL_BY_JOB_SUCEESS = "Lấy tất cả đơn ứng tuyển của công việc thành công";
    private static final String GET_ALL_BY_JOB_FAIL = "Lấy tất cả đơn ứng tuyển của công việc thất bại";
    private static final String REQUEST_METHOD_INVALID = "Phương thức không hợp lệ";

    private static final String CV_NOT_FOUND = "Không tìm thấy CV";

    private static final String JOB_NOT_FOUND = "Không tìm thấy công việc";
    private static final String USER_NOT_FOUND = "Không tìm thấy người dùng";

    private static final String HANDLE_APPLICATION_SUCCESS = "Xử lý đơn ứng tuyển thành công!";
    private static final String HANDLE_APPLICATION_FAILURE = "Xử lý đơn ứng tuyển không thành công!";


    private final JobApplicationMapper jobApplicationMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final AppUserRepository appUserRepository;
    private final JobRepository jobRepository;
    private final CvRepository cvRepository;
    private final MailUtils mailUtils;
    private final JobApplyRepository jobApplyRepository;


    public AppUser getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        return appUser.orElse(null);
    }


    public ServiceResponse createJobApplication(JobApplicationRequestBody jobApplicationRequestBody) {

        List<JobApplication> applications;
        JobApplication newJobApplication = jobApplicationMapper.jobApplicationRequestBodyToJobApplication(jobApplicationRequestBody);

       CV cv = cvRepository.findById(Long.valueOf(jobApplicationRequestBody.getCvId())).orElseThrow(() -> new ResourceNotFoundException(CV_NOT_FOUND));

       Job job =  jobRepository.findById(jobApplicationRequestBody.getJobId()).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));

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

        newJobApplication.setStatus(JobApplyStatus.PENDING);
        newJobApplication.setCv(cv);
        newJobApplication.setJob(job);

        System.out.println(newJobApplication.getJob().getCompany().getName());
        jobApplicationRepository.save(newJobApplication);

        mailUtils.sendMailJobApplycation(newJobApplication);

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


    public ServiceResponse getAllByCandidate() {
        try {
            AppUser user = getUser();

            List<JobApplication> listJobApplication = jobApplicationRepository.findByAppUserId(user.getId());
            List<JobApplicationDTO> jobApplicationDTOs = listJobApplication.stream()
                    .map(jobApplicationMapper::jobApplicationToJobApplicationDTO)
                    .toList();

            PagingResponseData pagingResponseData = PagingResponseData.builder()
                    .listData(jobApplicationDTOs)
                    .build();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_SUCCESS)
                    .data(Map.of("job_applications", pagingResponseData)).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException("Lấy thông tin đơn ứng tuyển thất bại");
        }
    }

    public ServiceResponse getAllJobApplicationByJob(Integer page, Integer size, Long jobId) {
        try {
            if (!checkValidJob(jobId)) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(JOB_NOT_FOUND)
                        .build();
            }


            Pageable pageable = PageRequest.of(page, size);
            Page<JobApplication> jobApplicationPage = jobApplicationRepository.findAllByJobId(jobId, pageable);


            PagingResponseData data = PagingResponseData.builder()
                    .totalPages(jobApplicationPage.getTotalPages())
                    .currentPage(jobApplicationPage.getNumber())
                    .totalItems(jobApplicationPage.getTotalElements())
                    .pageSize(jobApplicationPage.getSize())
                    .listData(jobApplicationPage.getContent().stream().map(jobApplicationMapper::jobApplicationToJobApplicationDTO))
                    .build();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_BY_JOB_SUCEESS)
                    .data(Map.of("job-applications", data)).build();

        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            throw new UndefinedException(GET_ALL_BY_JOB_FAIL);
        }

    }

    public ServiceResponse handleApplication(Long jobApplicationId, String status) {
        try {
            JobApplication jobApplication = jobApplyRepository.findById(jobApplicationId).orElse(null);

            assert jobApplication != null;
            jobApplication.setStatus(JobApplyStatus.valueOf(status));

            jobApplicationRepository.save(jobApplication);
            mailUtils.sendMailJobApplycationResult(jobApplication);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(HANDLE_APPLICATION_SUCCESS)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            throw new UndefinedException(HANDLE_APPLICATION_FAILURE);
        }
    }

    public AppUser getRecruiter() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(email).orElse(null);
    }

    public Boolean checkValidJob(Long jobId) {
        AppUser recruiter = getRecruiter();

        if (recruiter == null) {
            return false;
        }

        Job job = jobRepository.findById(jobId).orElse(null);

        if (job == null) {
            return false;
        }



        return true;
    }

}
