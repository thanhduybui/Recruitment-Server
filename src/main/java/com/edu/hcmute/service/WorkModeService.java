package com.edu.hcmute.service;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.WorkMode;
import com.edu.hcmute.mapper.WorkModeMapper;
import com.edu.hcmute.repository.WorkModeRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkModeService implements GenericService<OptionDTO, Integer> {
    private final WorkModeRepository workModeRepository;
    private final WorkModeMapper workModeMapper;
    private final RedisTemplate redisTemplate;
    private static final String GET_ALL_WORK_MODE_SUCCESS = "Lấy danh sách chế độ làm việc thành công";
    @Override
    public ServiceResponse getAll(Boolean isAll) {
        List<OptionDTO>  workModeDTOList;
        if (isAll) {
            List<WorkMode> workModes = this.workModeRepository.findAll();
            workModeDTOList = workModes.stream().map(workModeMapper::workModeToOptionDTO).toList();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_WORK_MODE_SUCCESS)
                    .data(Map.of("salary_ranges", workModeDTOList ))
                    .build();
        }

         workModeDTOList = redisTemplate.opsForList().range("activeWorkModes", 0, -1);
        if ( workModeDTOList == null ||   workModeDTOList.isEmpty()) {
            List<WorkMode> foundSalaryRanges = this.workModeRepository.findAllByStatus(Status.ACTIVE);
            workModeDTOList = foundSalaryRanges.stream().map(workModeMapper::workModeToOptionDTO).toList();
            redisTemplate.opsForList().rightPushAll("activeWorkModes", workModeDTOList);
        }

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_WORK_MODE_SUCCESS)
                .data(Map.of("work_modes", workModeDTOList))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        return null;
    }

    @Override
    public ServiceResponse create(OptionDTO object) {
        return null;
    }

    @Override
    public ServiceResponse update(OptionDTO object) {
        return null;
    }

    @Override
    public ServiceResponse delete(Integer id) {
        return null;
    }
}
