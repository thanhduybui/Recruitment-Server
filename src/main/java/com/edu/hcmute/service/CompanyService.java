package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.*;
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
import com.edu.hcmute.service.file.FileService;
import com.edu.hcmute.utils.ResponseUtils;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.edu.hcmute.constant.Message.USER_NOT_FOUND_BY_EMAIL;
import static com.edu.hcmute.service.file.FileService.MAX_FILE_SIZE;

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
    private static final String DELETE_BUSINESS_LICENSE_SUCCESS = "Xóa giấy phép kinh doanh thành công" ;
    private static final String DELETE_BUSINESS_LICENSE_FAIL = "Xóa giấy phép kinh doanh thất bại";
    private static final String ADD_BUSINESS_LICENSE_SUCCESS = "Thêm giấy phép kinh doanh thành công";
    private static final String GET_BUSINESS_LICENSE_SUCCESS = "Lấy giấy phép kinh doanh thành công";
    private static final String GET_BUSINESS_LICENSE_FAIL = "Lấy giấy phép kinh doanh thất bại";
    private static final String VERIFY_COMPANY_SUCCESS = "Xác thực công ty thành công";

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

        List<CandidateJobDTO> jobDTOs = companyJobs.getContent().stream().map(jobMapper::jobToCandidateJobDTO).collect(Collectors.toList());

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
        try{
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

        }catch (Exception e){
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

    public ServiceResponse uploadBusinessLicense(MultipartFile file) {
        List<String> extArray = Arrays.asList(".pdf", ".doc", ".docx");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email)));

            String fileExtension = fileService.getExtension(Objects.requireNonNull(file.getOriginalFilename()));

            if (!extArray.contains(fileExtension)) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_EXTENSION_NOT_SUPPORT)
                        .build();
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_SIZE_EXCEEDED_LIMIT)
                        .build();
            }

            String fileName = UUID.randomUUID() + fileExtension;
            String imgUrl = fileService.uploadFile(file, fileName);

            user.getCompany().setBusinessLicense(imgUrl);
            appUserRepository.save(user);


            return ResponseUtils.responseSuccess(HttpStatus.CREATED,
                    ResponseDataStatus.SUCCESS, ADD_BUSINESS_LICENSE_SUCCESS,
                    Map.of("business_license", imgUrl));

        } catch (Exception e) {
            log.error("Upload file failed: {}", e.getMessage());
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(Message.UPLOAD_FILE_FAILED)
                    .build();
        }
    }

    public ServiceResponse deleteBusinessLicense() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email)));

            user.getCompany().setBusinessLicense(null);
            user.getCompany().setIsVerified(false);
            appUserRepository.save(user);

            return ResponseUtils.responseSuccess(HttpStatus.OK,
                    ResponseDataStatus.SUCCESS, DELETE_BUSINESS_LICENSE_SUCCESS, null);

        } catch (Exception e) {
            log.error("Delete file failed: {}", e.getMessage());
           return ResponseUtils.responseFail(HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseDataStatus.ERROR, DELETE_BUSINESS_LICENSE_FAIL);
        }
    }

    public ServiceResponse getBusinessLicense() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email)));

            BusinessLicenseDTO buisinessLicenseDTO = BusinessLicenseDTO.builder()
                    .businessLicense(user.getCompany().getBusinessLicense())
                    .isVerified(user.getCompany().getIsVerified())
                    .build();


            return ResponseUtils.responseSuccess(HttpStatus.OK,
                    ResponseDataStatus.SUCCESS, GET_BUSINESS_LICENSE_SUCCESS,
                    Map.of("approval", buisinessLicenseDTO));

        } catch (Exception e) {
            log.error("Get file failed: {}", e.getMessage());
            return ResponseUtils.responseFail(HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseDataStatus.ERROR, GET_BUSINESS_LICENSE_FAIL);
        }
    }

    public ServiceResponse getAllCompanyForAdmin(Integer page, Integer size, Boolean verified) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<Company> companies = companyRepository.findAllByIsVerifiedAndStatusAndBusinessLicenseIsNotNull(verified, Status.ACTIVE, pageable );


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

        }catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(GET_ALL_COMPANY_FAIL);
        }
    }

    public ServiceResponse verifyCompany(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPANY_NOT_FOUND));

        company.setIsVerified(true);
        companyRepository.save(company);

        return ResponseUtils.responseSuccess(HttpStatus.OK,
                ResponseDataStatus.SUCCESS,VERIFY_COMPANY_SUCCESS, null);
    }
}
