package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {JobMapperHelper.class})
public interface JobMapper {
    @Mapping(source = "positionId", target = "position")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "salaryId", target = "salaryRange")
    @Mapping(source = "fieldId", target = "field")
    @Mapping(source = "majorId", target = "major")
    @Mapping(source = "experienceId", target = "experienceRange")
    @Mapping(source = "skillIds", target = "skills")
    @Mapping(source = "workModeId", target = "workMode")
    Job jobRequestBodyToJob(JobRequestBody jobRequest);
}
