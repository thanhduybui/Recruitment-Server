package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.SalaryRange;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SalaryRangeMapper {
    OptionDTO salaryRangeToOptionDTO(SalaryRange salaryRange);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSalaryRangeFromOptionDTO(OptionDTO optionDTO,@MappingTarget SalaryRange foundSalaryRange);
}
