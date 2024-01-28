package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.CvDTO;
import com.edu.hcmute.entity.CV;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CVMapper {
    @Mapping(source = "candidate.id", target = "candidateId")
    CvDTO toCvDTO(CV cv);
}
