package com.edu.hcmute.service;

import com.edu.hcmute.dto.FavoriteJobDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.FavoriteJob;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.FavoriteJobMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.FavoriteJobRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteJobService {

    private static final String ADD_FAVORITE_SUCCESS = "Thêm công việc yêu thích thành công";
    private static final String ADD_FAVORITE_FAIL = "Thêm công việc yêu thích thất bại";
    private static final String JOB_EXISTED_LIST = "Công việc này đã tồn tại trong mục yêu thích";
    private static final String UNAUTHORIZED = "Tài khoản không hợp lệ";

    private final FavoriteJobRepository favoriteJobRepository;
    private final FavoriteJobMapper favoriteJobMapper;
    private final AppUserRepository appUserRepository;

    public ServiceResponse addFavoriteJobToList(FavoriteJobDTO favoriteJobDTO) {
        try {
            FavoriteJob favoriteJob = favoriteJobMapper.favoriteJobDTOToFavoriteJob(favoriteJobDTO);
            AppUser user = getUser();

            if(user == null) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.INVALID)
                        .statusCode(HttpStatus.CREATED)
                        .message(UNAUTHORIZED)
                        .build();
            }

            if(checkJobExistedList(user.getId(),favoriteJob.getJob().getId())) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.ERROR)
                        .statusCode(HttpStatus.CREATED)
                        .message(JOB_EXISTED_LIST)
                        .build();
            }
            favoriteJob.setUser(user);
            favoriteJobRepository.save(favoriteJob);

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED)
                    .message(ADD_FAVORITE_SUCCESS)
                    .build();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(ADD_FAVORITE_FAIL);
        }
    }

    public AppUser getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(email).orElse(null);
    }

    public Boolean checkJobExistedList(Long userId, Long jobId) {
        List<FavoriteJob> favoriteJobList = favoriteJobRepository.findFavoriteJobByUserIdAndJobId(userId,jobId);
        return !favoriteJobList.isEmpty();
    }
}
