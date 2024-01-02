package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Position;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    OptionDTO positionToOptionDTO(Position position);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePositionFromOptionDTO(OptionDTO optionDTO,@MappingTarget Position position);
}
