package com.edu.hcmute.mapper;

import com.edu.hcmute.dto.FavoriteJobDTO;
import com.edu.hcmute.entity.FavoriteJob;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FavoriteJobMapper {

    @Mapping(source = "jobId", target = "job.id")
    FavoriteJob favoriteJobDTOToFavoriteJob(FavoriteJobDTO favoriteJobDTO);

}
