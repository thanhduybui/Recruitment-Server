package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.JobApplyDTO;
import com.edu.hcmute.entity.JobApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {JobApplyMapperHelper.class})
public interface JobApplyMapper {

    @Mapping(source = "jobId", target = "job")
    @Mapping(source = "cvId", target = "cv")
    JobApplication jobApplyDTOToJobApply(JobApplyDTO jobApplyDTO);
}
