package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    OptionDTO fieldToOptionDTO(Field field);
}
