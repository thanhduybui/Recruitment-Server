package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.entity.*;
import com.edu.hcmute.response.ShortData;
import org.mapstruct.*;

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

    JobDTO jobToJobDTO(Job job);

    ShortData companyToShortData(Company company);

    ShortData positionToShortData(Position position);

    ShortData fieldToShortData(Field field);
    ShortData salaryRangeToShortData(SalaryRange salaryRange);

    ShortData experienceRangeToShortData(ExperienceRange experienceRange);

    ShortData majorToShortData(Major major);

    ShortData workModeToShortData(WorkMode workMode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "positionId", target = "position")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "salaryId", target = "salaryRange")
    @Mapping(source = "fieldId", target = "field")
    @Mapping(source = "majorId", target = "major")
    @Mapping(source = "experienceId", target = "experienceRange")
    @Mapping(source = "skillIds", target = "skills")
    @Mapping(source = "workModeId", target = "workMode")
    void updateJobFromJobRequestBody(JobRequestBody jobRequestBody, @MappingTarget Job job);
}
