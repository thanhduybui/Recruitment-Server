package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.SalaryRange;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryRangeMapper {
    OptionDTO salaryRangeToOptionDTO(SalaryRange salaryRange);
}
