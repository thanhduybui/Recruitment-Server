package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.CompanyDTO;
import com.edu.hcmute.dto.CompanyRequestBody;
import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.entity.Job;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDTO companyToCompanyDTO(Company company);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyFromCompanyDTO(CompanyRequestBody companyDTO, @MappingTarget  Company company);
}
