package com.edu.hcmute.service;

import com.edu.hcmute.constant.Message;
import com.edu.hcmute.dto.CvDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.CVMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CvRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.file.FileService;
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
    private static final String GET_ONE_CV_FAILED = "Lấy CV thất bại";
    private static final String CV_NOT_FOUND = "Không tìm thấy CV";
    private static final String CREATED_CV_SUCCESS = "Tạo CV thành công";
    private static final String GET_ALL_CV_SUCCESS = "Lấy tất cả CV của người dùng thành công";
    private static final String GET_ONE_CV_SUCCESS = "Lấy CV thành công";
    private static final String DELETE_CV_SUCCESS = "Xóa CV thành công";

    private static final String DELETE_CV_FAILED = "Xóa CV thất bại";
    private static final String SET_DEFAULT_CV_SUCCESS = "Đặt CV mặc định thành công";
    private static final String SET_DEFAULT_CV_FAILED = "Đặt CV mặc định thất bại";

    private final CvRepository cvRepository;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    private final CVMapper cvMapper;
    private final List<String> extension = Arrays.asList(".pdf", ".doc", ".docx");

    public ServiceResponse uploadCV(MultipartFile multipartFile, String name) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CV cv = null;
        String fileUrl = "";
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


            fileUrl = fileService.uploadFile(multipartFile, fileName);






            if (email.equals("anonymousUser")) {
                cv = CV.builder()
                        .name(name)
                        .cvUrl(fileUrl)
                        .build();
            } else {
                AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                        () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email))
                );

                cv = CV.builder()
                        .name(name)
                        .cvUrl(fileUrl)
                        .isActive(true)
                        .candidate(user)
                        .build();
            }



            CV newCV = cvRepository.save(cv);

            CvDTO dataCV = cvMapper.CVtoCvDTO(newCV);


            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED)
                    .message(CREATED_CV_SUCCESS)
                    .data(Map.of("created_cv", dataCV))
                    .build();

        } catch (Exception e) {
            log.error("Upload file failed: " + e.getMessage());
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(CREATED_CV_FAILED)
                    .build();
        }
    }

    public ServiceResponse getOneCV(Long id) {
        try {
            CV cv = cvRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(CV_NOT_FOUND)
            );

            CvDTO dataCV = cvMapper.CVtoCvDTO(cv);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ONE_CV_SUCCESS)
                    .data(Map.of("cv", dataCV))
                    .build();
        } catch (Exception e) {
            log.error("Get CV failed: " + e.getMessage());
            throw new UndefinedException(GET_ONE_CV_FAILED);
        }
    }

    public ServiceResponse getUserCv() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email))
        );

        List<CV> cvs = cvRepository.findByCandidateAndIsActive(user, true);

        List<CvDTO> dataCV = cvs.stream().map(cvMapper::CVtoCvDTO).toList();

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_CV_SUCCESS)
                .data(Map.of("cvs", dataCV))
                .build();
    }

    public ServiceResponse deleteCV(Long id) {
        try {
            CV cv = cvRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(CV_NOT_FOUND)
            );

            cv.setIsActive(false);
            cvRepository.save(cv);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(DELETE_CV_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error("Delete CV failed: " + e.getMessage());
            throw new UndefinedException(DELETE_CV_FAILED);
        }
    }

    public ServiceResponse setDefaultCV(Long id) {
        try {
            CV cv = cvRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(CV_NOT_FOUND)
            );

            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, email))
            );

            List<CV> cvs = cvRepository.findByCandidateAndIsActive(user, true);

            for (CV c : cvs) {
                c.setIsDefault(false);
                cvRepository.save(c);
            }

            cv.setIsDefault(true);
            cvRepository.save(cv);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(SET_DEFAULT_CV_SUCCESS)
                    .build();
        } catch (Exception e) {
            log.error("Set default CV failed: " + e.getMessage());
            throw new UndefinedException(SET_DEFAULT_CV_FAILED);
        }
    }
}
