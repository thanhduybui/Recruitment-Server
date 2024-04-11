package com.edu.hcmute.service.user;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.AccountDTO;
import com.edu.hcmute.dto.ProfileDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.AppUserMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.FileServiceImpl;
import com.edu.hcmute.utils.BcryptUtils;
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
    private static final String INVALID_ROLE = "Không có quyền truy cập";
    private static final String DELETE_USER_SUCCESS = "Khóa tài khoản thành công";
    private static final String DELETE_USER_FAIL = "Khóa tài khoản không thành công";
    private static final String CREATE_ACCOUNT_SUCCESS = "Tạo tài khoản thành công";
    private static final String CREATE_ACCOUNT_FAIL = "Tạo tài khoản thất bại";
    private static final String CHANGE_ACCOUNT_SUCCESS = "Thay đổi tài khoản thành công";
    private static final String CHANGE_ACCOUNT_FAIL = "Thay đổi tài khoản không thành công";
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

            String fileExtension = fileService.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));

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
            log.error("Upload file failed: {}", e.getMessage());
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

    @Override
    public ServiceResponse deleteUser(Long userId, Status status) {

        try {

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            AppUser user = this.appUserRepository.findByEmail(email).orElse(null);

            if(user == null) {
                return responseFail(INVALID_ROLE);
            }

            if(user.getRole() == Role.ADMIN) {
                user.setStatus(status);
                appUserRepository.save(user);

                return responseFail(DELETE_USER_SUCCESS);
            }
            else if(user.getRole() == Role.CANDIDATE || user.getRole() == Role.RECRUITER) {

                if(Objects.equals(user.getId(), userId)) {
                    user.setStatus(Status.LOCK);

                    appUserRepository.save(user);

                    return responseFail(DELETE_USER_SUCCESS);
                }
                else {
                    return responseFail(INVALID_ROLE);
                }
            }

            return responseFail(INVALID_ROLE);
        }
        catch (Exception e) {
            log.error("Delete user failed: {}", e.getMessage());
            throw new UndefinedException(DELETE_USER_FAIL);
        }
    }

    @Override
    public ServiceResponse createUser(RegisterDTO registerDTO) {

       try {
           AppUser user = appUserRepository.findByEmail(registerDTO.getEmail().trim())
                   .orElse(null);

           if (user != null) {
              return responseFail(Message.EMAIL_ALREADY_EXISTS);
           }

           if (registerDTO.isPasswordMatching()) {
               return responseFail(Message.PASSWORD_NOT_MATCHING);
           }

           AppUser appUser = AppUser.builder()
                   .email(registerDTO.getEmail())
                   .password(BcryptUtils.hashPassword(registerDTO.getPassword()))
                   .fullName(registerDTO.getFullName())
                   .role(Role.valueOf(registerDTO.getRole()))
                   .build();

           appUserRepository.save(appUser);

           return responseSuccess(CREATE_ACCOUNT_SUCCESS);
       }
       catch (Exception e) {
           log.error("Create user failed: {}", e.getMessage());
           throw new UndefinedException(CREATE_ACCOUNT_FAIL);
       }
    }

    @Override
    public ServiceResponse changeInfoUser(AccountDTO accountDTO) {

        try{

            AppUser user = appUserRepository.findById(accountDTO.getId()).orElse(null);

            if(user == null) {
                return responseFail(USER_NOT_FOUND_BY_EMAIL);
            }

            user.setStatus(Status.valueOf(accountDTO.getStatus()));
            user.setEmail(accountDTO.getEmail());
            user.setFullName(accountDTO.getFullName());
            user.setRole(Role.valueOf(accountDTO.getRole()));
            user.setPassword(BcryptUtils.hashPassword(accountDTO.getPassword()));

            appUserRepository.save(user);

            return responseSuccess(CHANGE_ACCOUNT_SUCCESS);
        }
        catch (Exception e) {
            log.error("Change user failed: {}", e.getMessage());
            throw new UndefinedException(CHANGE_ACCOUNT_FAIL);
        }
    }

    public ServiceResponse responseFail(String message) {

        return ServiceResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST)
                .status(ResponseDataStatus.ERROR)
                .message(message)
                .build();
    }
    public ServiceResponse responseSuccess(String message) {

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(message)
                .build();
    }
}
