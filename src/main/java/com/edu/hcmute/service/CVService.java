package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.dto.CvRequestBody;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CVRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CVService {
    private static final String CREATE_CV_FAIL = "Tạo CV thất bại";
    private static final String CREATE_CV_SUCCESS = "Tạo CV thành công";

    private final AppUserRepository appUserRepository;
    private final CVRepository cvRepository;
    private final FileServiceImpl fileService;

    public ServiceResponse createCV(CvRequestBody cvRequestBody) {
        try{
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Message.USER_NOT_FOUND_BY_EMAIL, email)));

            String cvUrl = fileService.uploadFile(cvRequestBody.getCvFile(), "cv");

            CV cv = CV.builder()
                    .name(cvRequestBody.getName())
                    .candidate(user)
                    .cvUrl(cvUrl)
                    .build();
            cvRepository.save(cv);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED)
                    .message(CREATE_CV_SUCCESS)
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new UndefinedException(CREATE_CV_FAIL);
        }
    }
}
