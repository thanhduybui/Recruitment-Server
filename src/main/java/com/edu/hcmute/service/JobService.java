package com.edu.hcmute.service;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.dto.JobFilterCriteria;
import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.JobMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.JobRepository;
import com.edu.hcmute.response.PagingResponseData;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private static final String DELETE_JOB_SUCCESS = "Xóa công việc thành công";
    private static final String DELETE_JOB_FAIL = "Xóa công việc thất bại";
    private static final String REACH_LIMIT_POST = "Tài khoản của bạn chưa cung cấp giấy chứng nhận doanh nghiệp, bạn chỉ có thể đăng tối đa 3 công việc";

    private final JobRepository jobRepository;
    private final AppUserRepository appUserRepository;
    private final JobMapper jobMapper;
    public ServiceResponse create(JobRequestBody jobRequestBody) {
        try {
            if (!checkValidPost()){
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.INVALID)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(REACH_LIMIT_POST)
                        .build();
            }
            Job newJob = jobMapper.jobRequestBodyToJob(jobRequestBody);
            newJob.setStatus(Status.ACTIVE);

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

    private Boolean checkValidPost(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserRepository.findByEmail(email).orElse(null);
        if(user == null){
            return false;
        }

        Company checkedCompany = user.getCompany();

        if (checkedCompany.getJobs().size() >= 3){
            if (checkedCompany.getIsVerified()){
                return true;
            }
            return false;
        }
        return false;
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

    public ServiceResponse deleteJob(Long id){
        try{
            Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
            job.setStatus(Status.INACTIVE);
            jobRepository.save(job);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(DELETE_JOB_SUCCESS)
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new UndefinedException(DELETE_JOB_FAIL);
        }
    }

    public ServiceResponse getAll(Integer page, Integer size, JobFilterCriteria filterCriteria) {
        Instant conditionRenderTime = Instant.now().minusSeconds(24*7*60*60);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Job> jobs = jobRepository.findByFilterCriteria(filterCriteria.getKeyword(),
                        filterCriteria.getLocationId(),
                        filterCriteria.getWorkModeId(),
                        filterCriteria.getFieldId(),
                        filterCriteria.getMajorId(),
                        filterCriteria.getSalaryId(),
                        filterCriteria.getExperienceId(),
                        filterCriteria.getPositionId(),
                        filterCriteria.getHot(),
                        filterCriteria.getStatus(),
                        conditionRenderTime,
                        pageable);

            PagingResponseData data = PagingResponseData.builder()
                    .totalPages(jobs.getTotalPages())
                    .currentPage(jobs.getNumber())
                    .totalItems(jobs.getTotalElements())
                    .pageSize(jobs.getSize())
                    .listData(jobs.getContent().stream().map(jobMapper::jobToCandidateJobDTO))
                    .build();
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_JOB_SUCCESS)
                    .data(Map.of("jobs", data))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(GET_JOB_FAIL);
        }
    }
}
