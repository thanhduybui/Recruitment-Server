package com.edu.hcmute.service.user;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.dto.ProfileDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.AppUserMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private static final String UPDATE_USER_PROFILE_SUCCESS = "Cập nhật thông tin cá nhân thành công";
    private static final String GET_USER_PROFILE_SUCCESS = "Lấy thông tin cá nhân thành công";
    private static final String USER_NOT_FOUND_BY_EMAIL = "Không tìm thấy người dùng với email %s";
    private static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;
    private final FileServiceImpl fileService;
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    @Override
    public ServiceResponse changeUserAvatar(MultipartFile multipartFile) {
        List<String> extArray = Arrays.asList(".png", ".jpg", ".jpeg");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email)));

            String fileExtension = fileService.getExtension(multipartFile.getOriginalFilename());

            if (!extArray.contains(fileExtension)) {
                    return ServiceResponse.builder()
                            .status(ResponseDataStatus.ERROR)
                            .statusCode(HttpStatus.BAD_REQUEST)
                            .message(Message.FILE_EXTENSION_NOT_SUPPORT)
                            .build();
            }



            if (multipartFile.getSize() > MAX_FILE_SIZE) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_SIZE_EXCEEDED_LIMIT)
                        .build();
            }

            String fileName = UUID.randomUUID() + fileExtension;
            String imgUrl = fileService.uploadFile(multipartFile, fileName);

            user.setAvatar(imgUrl);
            appUserRepository.save(user);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(Message.UPLOAD_FILE_SUCCESS)
                    .data(Map.of("avatar", imgUrl))
                    .build();

        } catch (Exception e) {
            log.error("Upload file failed: " + e.getMessage());
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(Message.UPLOAD_FILE_FAILED)
                    .build();
        }
    }

    @Override
    public ServiceResponse updateUserProfile(ProfileDTO profileDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = this.appUserRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .message(String.format(Message.USER_NOT_FOUND_BY_EMAIL, email))
                    .build();
        }

        appUserMapper.updateAppUserFromRequest(profileDTO, user);

        this.appUserRepository.save(user);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(UPDATE_USER_PROFILE_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse getUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = this.appUserRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .message(String.format(Message.USER_NOT_FOUND_BY_EMAIL, email))
                    .build();
        }

        ProfileDTO profileDTO = appUserMapper.appUserToProfileDTO(user);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_USER_PROFILE_SUCCESS)
                .data(Map.of("profile", profileDTO))
                .build();
    }
}
