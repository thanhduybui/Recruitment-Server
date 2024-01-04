package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Field;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    OptionDTO fieldToOptionDTO(Field field);

    Field optionDTOToField(OptionDTO optionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFieldFromOptionDTO(OptionDTO optionDTO,@MappingTarget Field field);
}
