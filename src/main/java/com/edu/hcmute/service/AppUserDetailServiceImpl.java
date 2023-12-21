package com.edu.hcmute.service;


import com.edu.hcmute.config.security.AuthUser;
import com.edu.hcmute.constant.Message;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataSatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserDetailServiceImpl implements  AppUserService {

    private final FileService fileService;
    @Override
    public ServiceResponse changeUserAvatar(MultipartFile multipartFile) {
        try{
            String fileName = UUID.randomUUID().toString() + fileService.getExtension(multipartFile.getOriginalFilename());
            String IMG_URL = fileService.uploadFile(multipartFile, fileName);

            return ServiceResponse.builder()
                    .status(ResponseDataSatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(Message.UPLOAD_FILE_SUCCESS)
                    .data(Map.of("avatar", IMG_URL))
                    .build();

        }catch(Exception e){
            e.printStackTrace();
            return ServiceResponse.builder()
                    .status(ResponseDataSatus.ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(Message.UPLOAD_FILE_FAILED)
                    .build();
        }
    }
}
