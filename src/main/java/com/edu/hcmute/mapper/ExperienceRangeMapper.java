package com.edu.hcmute.mapper;

import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.ExperienceRange;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface ExperienceRangeMapper {
    OptionDTO experienceRangeToOptionDTO(ExperienceRange experienceRange);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateExperienceRangeFromOptionDTO(OptionDTO optionDTO,@MappingTarget ExperienceRange foundExperienceRange);

    ExperienceRange optionDTOToExperienceRange(OptionDTO optionDTO);
}
