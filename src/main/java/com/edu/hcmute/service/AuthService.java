package com.edu.hcmute.service;


import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
     ServiceResponse register(RegisterDTO registerDTO);
     ServiceResponse verifyRegister(String token);
}
