package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.CompanyDTO;
import com.edu.hcmute.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDTO companyToCompanyDTO(Company company);
}
