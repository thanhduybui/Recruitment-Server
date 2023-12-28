package com.edu.hcmute.service.auth;

import com.edu.hcmute.constant.Message;
import com.edu.hcmute.dto.LoginDTO;
import com.edu.hcmute.dto.RecruiterRegisterDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.VerifyDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataSatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.EmailSender;
import com.edu.hcmute.service.auth.AuthService;
import com.edu.hcmute.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterAuthServiceImpl implements AuthService<RecruiterRegisterDTO> {
    private final AppUserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final MailUtils mailUtils;
    @Override
    public ServiceResponse register(RecruiterRegisterDTO registerRecruiterDTO) {

        AppUser user = userRepository.findByEmail(registerRecruiterDTO.getEmail().trim())
                .orElse(null);


        if (user != null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.EMAIL_ALREADY_EXISTS)
                    .build();
        }

        if (!registerRecruiterDTO.isPasswordMatching()) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.PASSWORD_NOT_MATCHING)
                    .build();
        }

        redisTemplate.opsForValue().set(registerRecruiterDTO.getEmail(), registerRecruiterDTO, Duration.ofHours(8));
        mailUtils.generateCodeAndSendMail(registerRecruiterDTO.getEmail());

        return ServiceResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.REGISTER_SUCCESS)
                .data(registerRecruiterDTO)
                .build();
    }

    @Override
    public ServiceResponse verifyRegister(VerifyDTO verifyDTO) {
        return null;
    }

    @Override
    public ServiceResponse resendVerifyCode(String email) {
        return null;
    }

    @Override
    public ServiceResponse login(LoginDTO loginDTO) {
        return null;
    }

}
