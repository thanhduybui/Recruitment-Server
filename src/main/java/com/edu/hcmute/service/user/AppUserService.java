package com.edu.hcmute.service.user;

import com.edu.hcmute.dto.*;
import com.edu.hcmute.response.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;


public interface AppUserService {
    ServiceResponse changeUserAvatar(MultipartFile multipartFile);

    ServiceResponse updateUserProfile(ProfileDTO profileDTO);

    ServiceResponse getUserProfile();

    ServiceResponse deleteUser(Long userId, String status);

    ServiceResponse createUser(RegisterDTO registerDTO);

    ServiceResponse changeInfoUser(AccountDTO accountDTO);

    ServiceResponse resetPassword(ResetPasswordDTO resetPasswordDTO);

    ServiceResponse getAllUserByRole(Integer page, Integer size, String role);

    ServiceResponse searchUser(String email);

    ServiceResponse uploadBusinessLicense(MultipartFile multipartFile);

    ServiceResponse updateUserCvProfile(FindJobProfileRequestBody findJobProfileRequestBody);

    ServiceResponse getUserCvProfile();
}
