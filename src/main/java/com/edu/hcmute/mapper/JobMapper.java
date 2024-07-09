package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.CandidateJobDTO;
import com.edu.hcmute.dto.CompanyDTO;
import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.entity.*;
import com.edu.hcmute.response.ShortData;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {JobMapperHelper.class})
public interface JobMapper {
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "fieldId", target = "field")
    @Mapping(source = "majorId", target = "major")
    @Mapping(source = "experienceId", target = "experienceRange")
    @Mapping(source = "workModeId", target = "workMode")
    @Mapping(source = "locationId", target = "location")
    Job jobRequestBodyToJob(JobRequestBody jobRequest);

    @Mapping(source = "deadline", target = "restAppliedDays")
    JobDTO jobToJobDTO(Job job);

    CompanyDTO companyToCompanyDTO(Company company);

    ShortData fieldToShortData(Field field);

    ShortData experienceRangeToShortData(ExperienceRange experienceRange);

    ShortData majorToShortData(Major major);

    ShortData locationToShortData(Location location);

    ShortData workModeToShortData(WorkMode workMode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "fieldId", target = "field")
    @Mapping(source = "majorId", target = "major")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "experienceId", target = "experienceRange")
    @Mapping(source = "workModeId", target = "workMode")
    void updateJobFromJobRequestBody(JobRequestBody jobRequestBody, @MappingTarget Job job);


    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "company.image", target = "companyImage")
    @Mapping(source = "deadline", target = "restAppliedDays")
    @Mapping(source = "location.id", target = "locationId")
    CandidateJobDTO jobToCandidateJobDTO(Job job);

}
