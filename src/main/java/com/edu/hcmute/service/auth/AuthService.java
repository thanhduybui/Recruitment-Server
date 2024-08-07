package com.edu.hcmute.service.auth;


import com.edu.hcmute.dto.*;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ServiceResponse;
import com.google.auth.oauth2.OAuth2Credentials;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;

@Service
public interface AuthService<T> {
     ServiceResponse register(T registerDTO);

     ServiceResponse verifyRegister(VerifyDTO verifyDTO);

     ServiceResponse resendVerifyCode(String email);

     ServiceResponse login(LoginDTO loginDTO);

     ServiceResponse forgetPassword(ForgetPasswordDTO forgetPasswordDTO);

     ServiceResponse verifyForgetPassword(VerifyDTO verifyDTO);

     ServiceResponse updatePassword(UpdatePasswordDTO updatePasswordDTO);
     ServiceResponse googleAuth(TokenDTO tokenDTO);
}
