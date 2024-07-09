package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.*;
import com.edu.hcmute.entity.AppUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring",  uses = {JobMapperHelper.class})
public interface AppUserMapper {

    AppUser registerDTOToAppUser(RegisterDTO user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAppUserFromRequest(ProfileDTO profileDTO, @MappingTarget AppUser appUser);

    ProfileDTO appUserToProfileDTO(AppUser user);

    AppUser forgetPasswordDTOToAppUser(ForgetPasswordDTO user);

    UserDTO appUserToUserDTO(AppUser user);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "fieldId", target = "field")
    @Mapping(source = "majorId", target = "major")
    @Mapping(source = "locationId", target = "workLocation")
    AppUser updateUserCvProfileFromRequest(FindJobProfileRequestBody findJobProfileRequestBody, @MappingTarget  AppUser user);

    FindJobCvProfileDTO appUserToFindJobCvProfileDTO(AppUser user);

}
