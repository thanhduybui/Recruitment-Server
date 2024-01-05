package com.edu.hcmute.service;


import com.edu.hcmute.dto.CompanyDTO;
import com.edu.hcmute.dto.CompanyRequestBody;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.CompanyMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CompanyRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final AppUserRepository appUserRepository;
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private static final String UPDATE_COMPANY_SUCCESS = "Cập nhật thông tin công ty thành công";
    private static final String USER_NOT_FOUND = "Không tìm thấy người dùng để lấy thông tin công ty";
    private static final String GET_COMPANY_SUCCESS = "Lấy thông tin công ty thành công";

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
}
