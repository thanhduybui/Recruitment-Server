package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.SalaryRange;
import com.edu.hcmute.entity.WorkMode;
import org.hibernate.jdbc.Work;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface WorkModeMapper {

    OptionDTO workModeToOptionDTO(WorkMode workMode);

    WorkMode optionDTOToWorkMode(OptionDTO optionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWorkModeFromOptionDTO(OptionDTO object, @MappingTarget WorkMode workMode);
}
