package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.SalaryRange;
import com.edu.hcmute.entity.WorkMode;
import org.hibernate.jdbc.Work;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkModeMapper {

    OptionDTO workModeToOptionDTO(WorkMode workMode);
}
