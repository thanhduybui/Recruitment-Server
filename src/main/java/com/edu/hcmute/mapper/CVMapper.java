package com.edu.hcmute.mapper;

import com.edu.hcmute.dto.CvDTO;
import com.edu.hcmute.entity.CV;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CVMapper {

    CvDTO CVtoCvDTO(CV cv);

}
