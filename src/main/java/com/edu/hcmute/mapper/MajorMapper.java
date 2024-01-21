package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Field;
import com.edu.hcmute.entity.Major;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    OptionDTO majorToOptionDTO(Major major);

    Major optionDTOToMajor(OptionDTO optionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMajorFromOptionDTO(OptionDTO optionDTO,@MappingTarget Major major);
}
