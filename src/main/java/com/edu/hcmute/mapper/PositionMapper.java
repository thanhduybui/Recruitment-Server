package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.dto.PositionDTO;
import com.edu.hcmute.entity.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    OptionDTO positionToOptionDTO(Position position);
}
