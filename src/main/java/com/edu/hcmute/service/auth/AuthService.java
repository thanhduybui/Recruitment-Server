package com.edu.hcmute.service.auth;


import com.edu.hcmute.dto.LoginDTO;
import com.edu.hcmute.dto.RecruiterRegisterDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.VerifyDTO;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthService<T> {
     ServiceResponse register(T registerDTO);
     ServiceResponse verifyRegister(VerifyDTO verifyDTO);
     ServiceResponse resendVerifyCode(String email);
     ServiceResponse login(LoginDTO loginDTO);
}
