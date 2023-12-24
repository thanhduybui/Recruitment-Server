package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.response.ResponseDataSatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private final FileServiceImpl fileService;
    private static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    @Override
    public ServiceResponse changeUserAvatar(MultipartFile multipartFile) {
        try {
            String fileExtension = fileService.getExtension(multipartFile.getOriginalFilename());
            if (fileExtension != ".png" && fileExtension != ".jpg" && fileExtension != ".jpeg") {
                return ServiceResponse.builder()
                        .status(ResponseDataSatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_EXTENSION_NOT_SUPPORT)
                        .build();
            }

            if (multipartFile.getSize() > MAX_FILE_SIZE) {
                return ServiceResponse.builder()
                        .status(ResponseDataSatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_SIZE_EXCEEDED_LIMIT)
                        .build();
            }

            String fileName = UUID.randomUUID() + fileExtension;
            String imgUrl = fileService.uploadFile(multipartFile, fileName);
            return ServiceResponse.builder()
                    .status(ResponseDataSatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(Message.UPLOAD_FILE_SUCCESS)
                    .data(Map.of("avatar", imgUrl))
                    .build();

        } catch (Exception e) {
            log.error("Upload file failed: " + e.getMessage());
            return ServiceResponse.builder()
                    .status(ResponseDataSatus.ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(Message.UPLOAD_FILE_FAILED)
                    .build();
        }
    }
}
