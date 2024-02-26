package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.CompanyDTO;
import com.edu.hcmute.dto.CompanyRequestBody;
import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.CompanyMapper;
import com.edu.hcmute.mapper.JobMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CompanyRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private static final String UPDATE_COMPANY_SUCCESS = "Cập nhật thông tin công ty thành công";
    private static final String USER_NOT_FOUND = "Không tìm thấy người dùng để lấy thông tin công ty";
    private static final String GET_COMPANY_SUCCESS = "Lấy thông tin công ty thành công";
    private static final String GET_COMPANY_JOB_SUCCESS = "Lấy các công việc của công ty thành công";
    private static final String GET_ALL_COMPANY_JOB_SUCCESS = "Lấy danh sách công ty thành công";
    private static final String GET_ALL_COMPANY_FAIL = "Lấy danh sách công ty thất bại";
    private static final String COMPANY_NOT_FOUND = "Lấy thông tin công ty thành công";
    private static final String UPDATE_BUSINESS_LICENSE_SUCCESS = "Cập nhật giấy phép kinh doanh thành công";
    private static final String UPDATE_BUSINESS_LICENSE_FAIL = "Cập nhật giấy phép kinh doanh thất bại";
    private static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;
    private static final String FILE_EXTENSION_NOT_SUPPORT = "File không hợp lệ";
    private static final String DELETE_FILE_SUCCESS = "Xóa giấy xác nhận doanh nghiệp thành công";

    private final AppUserRepository appUserRepository;
    private final CompanyMapper companyMapper;
    private final JobMapper jobMapper;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final FileService fileService;

    public ServiceResponse getCompanyForUser(String email) {
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

    public ServiceResponse getCompanyJobs(Integer id, Integer page, Integer size, String type) {
        Pageable pageable = PageRequest.of(page, size);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPANY_NOT_FOUND));

        Instant now = Instant.now();

        Page<Job> companyJobs;
        switch (type) {
            case "ACTIVE":
                companyJobs = jobRepository.findAllByCompanyAndStatusAndDeadlineAfter(company, Status.ACTIVE, now, pageable);
                break;
            case "EXPIRED":
                companyJobs = jobRepository.findAllByCompanyAndDeadlineBefore(company, now, pageable);
                break;
            case "HOT":
                companyJobs = jobRepository.findAllByCompanyAndStatusAndIsHot(company, Status.ACTIVE, true, pageable);
                break;
            default:
                companyJobs = jobRepository.findAllByCompany(company, pageable);
                break;
        }

        List<JobDTO> jobDTOs = companyJobs.getContent().stream().map(jobMapper::jobToJobDTO).collect(Collectors.toList());

        PagingResponseData data = PagingResponseData.builder()
                .totalPages(companyJobs.getTotalPages())
                .totalItems(companyJobs.getTotalElements())
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

    public ServiceResponse getAllCompany(Integer page, Integer size, Boolean all) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Company> companies;
            if (all) {
                companies = companyRepository.findAll(pageable);
            } else {
                companies = companyRepository.findAllByStatus(Status.ACTIVE, pageable);
            }

            PagingResponseData data = PagingResponseData.builder()
                    .totalPages(companies.getTotalPages())
                    .totalItems(companies.getTotalElements())
                    .currentPage(companies.getNumber())
                    .pageSize(companies.getSize())
                    .listData(companies.getContent().stream().map(companyMapper::companyToCompanyDTO).collect(Collectors.toList()))
                    .build();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_COMPANY_JOB_SUCCESS)
                    .data(Map.of("companies", data))
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(GET_ALL_COMPANY_FAIL);
        }

    }

    public ServiceResponse getOneCompany(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPANY_NOT_FOUND));

        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_COMPANY_SUCCESS)
                .data(Map.of("company", companyDTO))
                .build();
    }

    public ServiceResponse uploadBusinessFile(MultipartFile multipartFile) {
        try {
            List<String> extArray = Arrays.asList(".docx", ".doc", ".pdf");
            String fileExtension = fileService.getExtension(multipartFile.getOriginalFilename());
            if (!extArray.contains(fileExtension)) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(FILE_EXTENSION_NOT_SUPPORT)
                        .build();
            }

            if (multipartFile.getSize() > MAX_FILE_SIZE) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_SIZE_EXCEEDED_LIMIT)
                        .build();
            }

            String fileName = UUID.randomUUID() + fileExtension;
            String fileUrl = fileService.uploadFile(multipartFile, fileName);
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            AppUser appUser = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
            appUser.getCompany().setBusinessLicense(fileUrl);

            companyRepository.save(appUser.getCompany());
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(UPDATE_BUSINESS_LICENSE_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(UPDATE_BUSINESS_LICENSE_FAIL);
        }
    }

    public ServiceResponse deleteBusinessFile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        appUser.getCompany().setBusinessLicense(null);
        companyRepository.save(appUser.getCompany());
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(DELETE_FILE_SUCCESS)
                .build();
    }
}
