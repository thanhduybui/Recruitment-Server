package com.edu.hcmute.service;

import com.edu.hcmute.constant.Message;
import com.edu.hcmute.dto.CvDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.CVMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CvRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.edu.hcmute.constant.Message.USER_NOT_FOUND_BY_EMAIL;


@Service
@RequiredArgsConstructor
@Slf4j
public class CVService {
    private static final String CREATED_CV_FAILED = "Tạo CV thất bại";
    private final CvRepository cvRepository;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    private final CVMapper cvMapper;
    private final List<String> extension = Arrays.asList(".pdf", ".doc", ".docx");

    public ServiceResponse uploadCV(MultipartFile multipartFile, String name) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CV cv = null;
        try {

            String fileExtension = fileService.getExtension(multipartFile.getOriginalFilename());

            if (!extension.contains(fileExtension)) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_EXTENSION_NOT_SUPPORT)
                        .build();
            }

            if (multipartFile.getSize() > FileService.MAX_FILE_SIZE) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(Message.FILE_SIZE_EXCEEDED_LIMIT)
                        .build();
            }


            String fileName = UUID.randomUUID() + fileExtension;
            String fileUrl = fileService.uploadFile(multipartFile, fileName);


            if (email.equals("anonymousUser")){
                cv = CV.builder()
                        .name(name)
                        .cvUrl(fileUrl)
                        .build();
            }else {
                AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                        () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email))
                );

                cv = CV.builder()
                        .name(name)
                        .cvUrl(fileUrl)
                        .candidate(user)
                        .build();
            }

            CV newCV = cvRepository.save(cv);

            CvDTO dataCV = cvMapper.CVtoCvDTO(newCV);


            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED)
                    .message(Message.UPLOAD_FILE_SUCCESS)
                    .data(Map.of("created_cv",dataCV))
                    .build();

        }catch (Exception e) {
            log.error("Upload file failed: " + e.getMessage());
           return ServiceResponse.builder()
                   .status(ResponseDataStatus.ERROR)
                   .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                   .message(CREATED_CV_FAILED)
                   .build();
        }
    }
}
