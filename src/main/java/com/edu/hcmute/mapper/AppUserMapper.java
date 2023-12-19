package com.edu.hcmute.mapper;


import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser registerDTOToAppUser(RegisterDTO user);
}
