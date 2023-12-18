package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.mapper.AppUserMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataSatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.utils.BcryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;


    @Override
    public ServiceResponse register(RegisterDTO registerDTO) {
        AppUser user = userRepository.findByEmail(registerDTO.getEmail().trim())
                                .orElse(null);
        if (user != null){
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.EMAIL_ALREADY_EXISTS)
                    .build();
        }

        if (!registerDTO.isPasswordMatching()){
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.PASSWORD_NOT_MATCHING)
                    .build();
        }

        AppUser newUser = userMapper.registerDTOToAppUser(registerDTO);
        newUser.setPassword(BcryptUtils.hashPassword(registerDTO.getPassword().trim()));
        userRepository.save(newUser);

        return ServiceResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.REGISTER_SUCCESS)
                .data(registerDTO)
                .build();
    }
}
