package com.edu.hcmute.service;

import com.edu.hcmute.dto.FavoriteJobDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.FavoriteJob;
import com.edu.hcmute.exception.UndefinedException;
import com.edu.hcmute.mapper.FavoriteJobMapper;
import com.edu.hcmute.mapper.JobMapper;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.FavoriteJobRepository;
import com.edu.hcmute.response.PagingResponseData;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteJobService {

    private static final String ADD_FAVORITE_SUCCESS = "Thêm công việc yêu thích thành công";
    private static final String ADD_FAVORITE_FAIL = "Thêm công việc yêu thích thất bại";
    private static final String JOB_EXISTED_LIST = "Công việc này đã tồn tại trong mục yêu thích";
    private static final String UNAUTHORIZED = "Tài khoản không hợp lệ";
    private static final String GET_ALL_SUCCESS = "Lấy danh sách yêu thích thành công";
    private static final String GET_ALL_FAIL = "Lấy danh sách yêu thích thành công";

    private final FavoriteJobRepository favoriteJobRepository;
    private final FavoriteJobMapper favoriteJobMapper;
    private final AppUserRepository appUserRepository;

    private final JobMapper jobMapper;

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

    public ServiceResponse getAllByUser(Integer page, Integer size) {
        Instant conditionRenderTime = Instant.now().minusSeconds(24*7*60*60);

        try {

            AppUser user = getUser();

            if(user == null) {
                return ServiceResponse.builder()
                        .status(ResponseDataStatus.INVALID)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(UNAUTHORIZED)
                        .build();
            }

            System.out.println(user.getId());

            Pageable pageable = PageRequest.of(page,size);
            Page<FavoriteJob> favoriteJobPage = favoriteJobRepository.findAllByUser(user,pageable);

            PagingResponseData data = PagingResponseData.builder()
                    .totalPages(favoriteJobPage.getTotalPages())
                    .currentPage(favoriteJobPage.getNumber())
                    .totalItems(favoriteJobPage.getTotalElements())
                    .pageSize(favoriteJobPage.getSize())
                    .listData(favoriteJobPage.getContent().stream().map(favoriteJobMapper::favoriteJobJobToCandidateDTO))
                    .build();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_SUCCESS)
                    .data(Map.of("favorite_jobs", data))
                    .build();

        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new UndefinedException(GET_ALL_FAIL);
        }
    }
}
