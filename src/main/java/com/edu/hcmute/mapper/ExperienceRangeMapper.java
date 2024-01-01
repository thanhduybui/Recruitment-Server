package com.edu.hcmute.mapper;

import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.ExperienceRange;
import com.edu.hcmute.entity.SalaryRange;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ExperienceRangeMapper {
    OptionDTO experienceRangeToOptionDTO(ExperienceRange experienceRange);
}
