package com.edu.hcmute.mapper;

import com.edu.hcmute.dto.GetJobApplicationDTO;
import com.edu.hcmute.dto.JobApplicationDTO;
import com.edu.hcmute.dto.JobApplicationRequestBody;
import com.edu.hcmute.entity.JobApplication;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {JobApplicationMapperHelper.class})
public interface JobApplicationMapper {
    @Mapping(source = "jobId" , target = "job.id")
    @Mapping(source = "cvId" , target = "cv.id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "name" , target = "name")
    JobApplication jobApplicationRequestBodyToJobApplication(JobApplicationRequestBody jobApplicationRequestBody);

    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "cv.id" , target = "cvId")
    @Mapping(source = "applyStatus" , target = "jobApplicationStatus")
    @Mapping(source = "createdAt" , target = "createAt")
    @Mapping(source = "updatedAt" , target = "updateAt")
    GetJobApplicationDTO jobApplicationToGetJobApplicationDTO(JobApplication jobApplication);

    JobApplicationDTO jobApplicationToJobApplicationDTO(JobApplication jobApplication);
}
