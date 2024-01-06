package com.edu.hcmute.service;


import com.edu.hcmute.dto.CompanyDTO;
import com.edu.hcmute.dto.CompanyRequestBody;
import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.CompanyMapper;
import com.edu.hcmute.mapper.JobMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CompanyRepository;
import com.edu.hcmute.repository.JobRepository;
import com.edu.hcmute.response.PagingResponseData;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final AppUserRepository appUserRepository;
    private final CompanyMapper companyMapper;
    private final JobMapper jobMapper;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private static final String UPDATE_COMPANY_SUCCESS = "Cập nhật thông tin công ty thành công";
    private static final String USER_NOT_FOUND = "Không tìm thấy người dùng để lấy thông tin công ty";
    private static final String GET_COMPANY_SUCCESS = "Lấy thông tin công ty thành công";
    private static final String GET_COMPANY_JOB_SUCCESS = "Lấy các công việc của công ty thành công";
    private static final String COMPANY_NOT_FOUND = "Lấy thông tin công ty thành công";

    public ServiceResponse getCompanyForUser(String email){
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        Company company = appUser.getCompany();

        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_COMPANY_SUCCESS)
                .data(Map.of("company", companyDTO))
                .build();

    }

    public ServiceResponse updateCompanyForUser(String email, CompanyRequestBody companyDTO) {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        Company company = appUser.getCompany();

        companyMapper.updateCompanyFromCompanyDTO(companyDTO, company);

        this.companyRepository.save(company);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(UPDATE_COMPANY_SUCCESS)
                .build();

    }

    public ServiceResponse getCompanyJobs(Integer id, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPANY_NOT_FOUND));

        Page<Job> companyJobs = jobRepository.findAllByCompany(company, pageable);

        List<JobDTO> jobDTOs = companyJobs.getContent().stream().map(jobMapper::jobToJobDTO).collect(Collectors.toList());

        PagingResponseData data = PagingResponseData.builder()
                .totalPage(companyJobs.getTotalPages())
                .totalItem(companyJobs.getTotalElements())
                .currentPage(companyJobs.getNumber())
                .pageSize(companyJobs.getSize())
                .listData(jobDTOs)
                .build();

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_COMPANY_JOB_SUCCESS)
                .data(Map.of("jobs", data))
                .build();
    }
}
