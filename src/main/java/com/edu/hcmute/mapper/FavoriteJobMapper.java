package com.edu.hcmute.mapper;

import com.edu.hcmute.dto.CandidateJobDTO;
import com.edu.hcmute.dto.FavoriteJobDTO;
import com.edu.hcmute.dto.JobDTO;
import com.edu.hcmute.entity.FavoriteJob;
import com.edu.hcmute.entity.Job;
import org.mapstruct.*;

@Mapper(componentModel = "spring" , uses = {JobMapperHelper.class})
public interface FavoriteJobMapper {

    @Mapping(source = "jobId", target = "job.id")
    FavoriteJob favoriteJobDTOToFavoriteJob(FavoriteJobDTO favoriteJobDTO);

    @Mapping(source = "job.company.name", target = "companyName")
    @Mapping(source = "job.title", target = "title")
    @Mapping(source = "job.isHot" , target = "isHot")
    @Mapping(source = "job.company.image", target = "companyImage")
    @Mapping(source = "job.salaryRange.name", target = "salaryRange")
    @Mapping(source = "job.deadline", target = "restAppliedDays")
    @Mapping(source = "job.location.id", target = "locationId")
    @Mapping(source = "job.id", target = "id")
    CandidateJobDTO favoriteJobJobToCandidateDTO(FavoriteJob favoriteJob);
}

