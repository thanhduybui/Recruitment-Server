package com.edu.hcmute.service.user;

import com.edu.hcmute.config.security.AuthUser;
import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.AccountDTO;
import com.edu.hcmute.dto.ProfileDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.ResetPasswordDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface AppUserService {
   ServiceResponse changeUserAvatar(MultipartFile multipartFile);

    ServiceResponse updateUserProfile(ProfileDTO profileDTO);
    ServiceResponse getUserProfile();
    ServiceResponse deleteUser(Long userId, String status);
    ServiceResponse createUser(RegisterDTO registerDTO);
    ServiceResponse changeInfoUser(AccountDTO accountDTO);
    ServiceResponse resetPassword(ResetPasswordDTO resetPasswordDTO);
}
