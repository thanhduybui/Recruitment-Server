package com.edu.hcmute.service.user;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.*;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.AppUserMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.PagingResponseData;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.file.FileServiceImpl;
import com.edu.hcmute.utils.BcryptUtils;
import com.edu.hcmute.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    private static final String RESET_PASSWORD_SUCCESS = "Đặt lại mật khẩu thành công";
    private static final String RESET_PASSWORD_FAIL = "Đặt lại mật khẩu thất bại";
    private static final String INCORRECT_PASSWORD = "Mật khẩu không đúng";
    private static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;
    private static final String GET_ALL_USER_SUCCESS = "Lấy tất cả người dùng thành công";
    private static final String GET_ALL_USER_FAIL = "Lấy tất cả người dùng thất bại";
    private static final String FOUND_USER = "Tìm thấy người dùng";


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
                    .message(String.format(USER_NOT_FOUND_BY_EMAIL, email))
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
                    .message(String.format(USER_NOT_FOUND_BY_EMAIL, email))
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
    public ServiceResponse deleteUser(Long userId, String status) {

        try {

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            AppUser user = this.appUserRepository.findByEmail(email).orElse(null);

            AppUser deleteUser = this.appUserRepository.findById(userId).orElse(null);

            if (user == null) {
                return responseFail(INVALID_ROLE);
            }

            if (deleteUser == null) {
                return responseFail(Message.USER_NOT_FOUND_BY_EMAIL);
            } else {
                if (user.getRole() == Role.ADMIN) {

                    deleteUser.setStatus(Status.valueOf(status));
                    appUserRepository.save(deleteUser);

                    return responseSuccess(DELETE_USER_SUCCESS);
                } else if (user.getRole() == Role.CANDIDATE || user.getRole() == Role.RECRUITER) {

                    if (Objects.equals(user.getId(), userId)) {
                        deleteUser.setStatus(Status.LOCK);
                        appUserRepository.save(deleteUser);

                        return responseSuccess(DELETE_USER_SUCCESS);
                    } else {
                        return responseFail(INVALID_ROLE);
                    }
                }
            }

            return responseFail(INVALID_ROLE);
        } catch (Exception e) {
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
                    .status(Status.ACTIVE)
                    .build();

            appUserRepository.save(appUser);

            return responseSuccess(CREATE_ACCOUNT_SUCCESS);
        } catch (Exception e) {
            log.error("Create user failed: {}", e.getMessage());
            throw new UndefinedException(CREATE_ACCOUNT_FAIL);
        }
    }

    @Override
    public ServiceResponse changeInfoUser(AccountDTO accountDTO) {

        try {

            AppUser user = appUserRepository.findById(accountDTO.getId()).orElse(null);


            if (user == null) {
                return responseFail(USER_NOT_FOUND_BY_EMAIL);
            }

            if (accountDTO.getStatus() != null) {
                user.setStatus(Status.valueOf(accountDTO.getStatus()));
            }
            if (accountDTO.getRole() != null) {
                user.setRole(Role.valueOf(accountDTO.getRole()));
            }
            if (accountDTO.getEmail() != null) {
                user.setEmail(accountDTO.getEmail());
            }
            if (accountDTO.getFullName() != null) {
                user.setFullName(accountDTO.getFullName());
            }
            if (accountDTO.getPassword() != null) {
                user.setPassword(BcryptUtils.hashPassword(accountDTO.getPassword()));
            }

            System.out.println(user);

            appUserRepository.save(user);

            return responseSuccess(CHANGE_ACCOUNT_SUCCESS);
        } catch (Exception e) {

            log.error("Change user failed: {}", e.getMessage());
            throw new UndefinedException(CHANGE_ACCOUNT_FAIL);
        }
    }

    @Override
    public ServiceResponse resetPassword(ResetPasswordDTO resetPasswordDTO) {

        try {

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            AppUser user = this.appUserRepository.findByEmail(email).orElse(null);

            if (user == null) {
                return responseFail(INVALID_ROLE);
            }

            // False
            if (resetPasswordDTO.isPasswordMatching()) {
                return responseFail(Message.PASSWORD_NOT_MATCHING);
            }

            // True
            if (!BcryptUtils.verifyPassword(resetPasswordDTO.getOld_password(), user.getPassword())) {
                return responseFail(INCORRECT_PASSWORD);
            }

            user.setPassword(BcryptUtils.hashPassword(resetPasswordDTO.getNew_password()));
            appUserRepository.save(user);

            return responseSuccess(RESET_PASSWORD_SUCCESS);
        } catch (Exception e) {
            log.error("Reset password failed: {}", e.getMessage());
            throw new UndefinedException(RESET_PASSWORD_FAIL);
        }
    }

    @Override
    public ServiceResponse getAllUserByRole(Integer page, Integer size, String role) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AppUser> appUsers;

            if (role == null) {
                appUsers = appUserRepository.findAll(pageable);
            } else {
                appUsers = appUserRepository.findAllByRole(Role.valueOf(role), pageable);
            }

            List<UserDTO> userDTOS = appUsers.getContent().stream()
                    .map(appUserMapper::appUserToUserDTO)
                    .toList();

            PagingResponseData pagingResponseData = PagingResponseData.builder()
                    .totalPages(appUsers.getTotalPages())
                    .totalItems(appUsers.getTotalElements())
                    .currentPage(appUsers.getNumber())
                    .pageSize(appUsers.getSize())
                    .listData(userDTOS)
                    .build();

            return ResponseUtils.responseSuccess(HttpStatus.OK, ResponseDataStatus.SUCCESS, GET_ALL_USER_SUCCESS, pagingResponseData);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(GET_ALL_USER_FAIL);
        }
    }

    @Override
    public ServiceResponse searchUser(String email) {
        try {
            List<AppUser> users = appUserRepository.findByEmailLike(email);
            if (users.isEmpty()) {
                throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
            }

            List<UserDTO> userDTOs = users.stream()
                    .map(appUserMapper::appUserToUserDTO)
                    .collect(Collectors.toList());

            return ResponseUtils.responseSuccess(HttpStatus.OK, ResponseDataStatus.SUCCESS, FOUND_USER, userDTOs);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException("Lỗi không xác định");
        }
    }

    @Override
    public ServiceResponse uploadBusinessLicense(MultipartFile multipartFile) {
        // Hiện tại chỉ hỗ trợ file pdf
        List<String> extArray = Arrays.asList(".pdf");
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

            user.getCompany().setBusinessLicense(imgUrl);
            appUserRepository.save(user);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(Message.UPLOAD_FILE_SUCCESS)
                    .data(Map.of("business_license", imgUrl))
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
    public ServiceResponse updateUserCvProfile(FindJobProfileRequestBody findJobProfileRequestBody) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            AppUser user = appUserRepository.findByEmail(email).orElse(null);

            if (user == null) {
                return responseFail(Message.USER_NOT_FOUND_BY_EMAIL);
            }

            user = appUserMapper.updateUserCvProfileFromRequest(findJobProfileRequestBody, user);

            appUserRepository.save(user);

            return responseSuccess(Message.UPDATE_USER_CV_SUCCESS);


        } catch (Exception e) {
            log.error("Update user cv failed: {}", e.getMessage());
            throw new UndefinedException("Cập nhật thông tin cv thất bại");
        }
    }

    @Override
    public ServiceResponse getUserCvProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = this.appUserRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .message(String.format(USER_NOT_FOUND_BY_EMAIL, email))
                    .build();
        }

        FindJobCvProfileDTO findJobCvProfileDTO = appUserMapper.appUserToFindJobCvProfileDTO(user);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_USER_PROFILE_SUCCESS)
                .data(Map.of("profile", findJobCvProfileDTO))
                .build();
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
