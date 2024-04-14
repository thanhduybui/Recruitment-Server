package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.ForgetPasswordDTO;
import com.edu.hcmute.dto.ProfileDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.UserDTO;
import com.edu.hcmute.entity.AppUser;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser registerDTOToAppUser(RegisterDTO user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAppUserFromRequest(ProfileDTO profileDTO, @MappingTarget AppUser appUser);

    ProfileDTO appUserToProfileDTO(AppUser user);

    AppUser forgetPasswordDTOToAppUser(ForgetPasswordDTO user);

    UserDTO appUserToUserDTO(AppUser user);

}
