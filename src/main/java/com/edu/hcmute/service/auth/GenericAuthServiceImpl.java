package com.edu.hcmute.service.auth;

import com.edu.hcmute.constant.Gender;
import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.dto.*;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Company;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.utils.BcryptUtils;
import com.edu.hcmute.utils.CustomClaim;
import com.edu.hcmute.utils.JwtUtils;
import com.edu.hcmute.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericAuthServiceImpl<T extends RegisterContainer> implements AuthService<T> {

    private final AppUserRepository userRepository;
    @SuppressWarnings("rawtypes")
    private final RedisTemplate redisTemplate;
    private final MailUtils mailUtils;

    private static  final String ACCOUNT_NOT_EXISTED = "Tài khoản không tồn tại";
    private static final String SEND_CODE_SUCCESS = "Đã gửi mã về email thành công";
    private static final String SET_PASSWORD_SUCCESS = "Đặt password thành công";
    private static final String SET_PASSWORD_FAIL = "Đặt password thất bại";
    private static final String CHANGE_ACCOUNT_SUCCESS = "Cập nhật tài khoản thành công";
    private static final String CHANGE_ACCOUNT_FAIL = "Cập nhật tài khoản thất bại";

    @SuppressWarnings("unchecked")
    @Override
    public ServiceResponse register(T registerDTO) {


        AppUser user = userRepository.findByEmail(registerDTO.getEmail().trim())
                .orElse(null);


        if (user != null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.EMAIL_ALREADY_EXISTS)
                    .build();
        }

        if (registerDTO.isPasswordMatching()) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.PASSWORD_NOT_MATCHING)
                    .build();
        }

        redisTemplate.opsForValue().set(registerDTO.getEmail(), registerDTO, Duration.ofHours(8));
        mailUtils.generateCodeAndSendMail(registerDTO.getEmail());

        return ServiceResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .status(ResponseDataStatus.SUCCESS)
                .message(Message.REGISTER_SUCCESS)
                .data(registerDTO)
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ServiceResponse verifyRegister(VerifyDTO verifyDTO) {
        Object dto = redisTemplate.opsForValue().get(verifyDTO.getEmail());

        if (dto == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.EXPIRED_VERIFICATION_TIME)
                    .build();
        }

        String verifyCode = (String) redisTemplate.opsForValue().get(verifyDTO.getEmail() + "_otp");
        log.info("verifyCodeRe: {}", verifyCode);


        if (verifyCode == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.EXPIRED_OTP)
                    .build();
        }

        if (!verifyDTO.getOtp().equals(verifyCode)) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.INVALID_OTP)
                    .build();
        }

        AppUser user = null;
        if (dto instanceof RegisterDTO registerDTO) {
            user = AppUser.builder()
                    .email(registerDTO.getEmail())
                    .password(BcryptUtils.hashPassword(registerDTO.getPassword()))
                    .fullName(registerDTO.getFullName())
                    .role(Role.CANDIDATE)
                    .build();
        } else if (dto instanceof RecruiterRegisterDTO recruiterRegisterDTO) {
            Company company = Company.builder()
                    .name(recruiterRegisterDTO.getCompanyName())
                    .build();
            user = AppUser.builder()
                    .email(recruiterRegisterDTO.getEmail())
                    .password(BcryptUtils.hashPassword(recruiterRegisterDTO.getPassword()))
                    .fullName(recruiterRegisterDTO.getFullName())
                    .gender(Gender.valueOf(recruiterRegisterDTO.getGender()))
                    .company(company)
                    .role(Role.RECRUITER)
                    .build();
        }

        assert user != null;
        userRepository.save(user);

        // delete otp save in redis
        redisTemplate.delete(verifyDTO.getEmail() + "_otp");
        //delete registerDTO save in redis
        redisTemplate.delete(verifyDTO.getEmail());

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(Message.VERIFY_SUCCESS)
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ServiceResponse resendVerifyCode(String email) {
        Object dto = redisTemplate.opsForValue().get(email);
        // check if registerDTO is expired
        if (dto == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.EXPIRED_VERIFICATION_TIME)
                    .build();


        }
        // delete old otp
        redisTemplate.delete(email + "_otp");
        mailUtils.generateCodeAndSendMail(email);
        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(Message.RESEND_OTP_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse login(LoginDTO loginDTO) {
        AppUser user = userRepository.findByEmail(loginDTO.getEmail())
                .orElse(null);
        if (user == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.ACCOUNT_NOT_FOUND)
                    .build();
        }

        if (!BcryptUtils.verifyPassword(loginDTO.getPassword(), user.getPassword())) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.PASSWORD_NOT_MATCHING)
                    .build();
        }

        CustomClaim customClaim = CustomClaim.builder()
                .email(user.getEmail())
                .role(user.getRole() + "")
                .build();

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(Message.LOGIN_SUCCESS)
                .data(Map.of("access_token", JwtUtils.generateToken(customClaim)))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ServiceResponse forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        AppUser user = userRepository.findByEmail(forgetPasswordDTO.getEmail().trim())
                .orElse(null);

        if (user != null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(ACCOUNT_NOT_EXISTED)
                    .build();
        }

        redisTemplate.opsForValue().set(forgetPasswordDTO.getEmail(), forgetPasswordDTO, Duration.ofMinutes(70));
        mailUtils.generateCodeAndSendMail(forgetPasswordDTO.getEmail());

        return ServiceResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .status(ResponseDataStatus.SUCCESS)
                .message(SEND_CODE_SUCCESS)
                .data(forgetPasswordDTO)
                .build();
    }

    @Override
    public ServiceResponse verifyForgetPassword(VerifyDTO verifyDTO) {

        Object dto = redisTemplate.opsForValue().get(verifyDTO.getEmail());

        if (dto == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.EXPIRED_VERIFICATION_TIME)
                    .build();
        }

        String verifyCode = (String) redisTemplate.opsForValue().get(verifyDTO.getEmail() + "_otp");
        log.info("verifyCode: {}", verifyCode);


        if (verifyCode == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.EXPIRED_OTP)
                    .build();
        }

        if (!verifyDTO.getOtp().equals(verifyCode)) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataStatus.ERROR)
                    .message(Message.INVALID_OTP)
                    .build();
        }

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(Message.VERIFY_SUCCESS)
                .data(verifyDTO.getEmail())
                .build();
    }

    @Override
    public ServiceResponse updatePassword(UpdatePasswordDTO updatePasswordDTO) {

        try {

            AppUser appUser = userRepository.findByEmail(updatePasswordDTO.getEmail()).orElse(null);

            if (!updatePasswordDTO.isPasswordMatching()) {
                return ServiceResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .status(ResponseDataStatus.ERROR)
                        .message(Message.PASSWORD_NOT_MATCHING)
                        .build();
            }

            assert appUser != null;
            appUser.setPassword(BcryptUtils.hashPassword(updatePasswordDTO.getPassword()));

            userRepository.save(appUser);

            CustomClaim customClaim = CustomClaim.builder()
                    .email(appUser.getEmail())
                    .role(appUser.getRole() + "")
                    .build();

            return ServiceResponse.builder()
                    .statusCode(HttpStatus.OK)
                    .status(ResponseDataStatus.SUCCESS)
                    .message(SET_PASSWORD_SUCCESS)
                    .data(Map.of("access_token", JwtUtils.generateToken(customClaim)))
                    .build();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(SET_PASSWORD_FAIL);
        }

    }
}
