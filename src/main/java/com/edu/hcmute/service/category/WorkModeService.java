package com.edu.hcmute.service.category;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.WorkMode;
import com.edu.hcmute.exception.ResourceNotFoundException;
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
    private static final String GET_ALL_WORK_MODE_SUCCESS = "Lấy danh sách chế độ làm việc thành công";
    private static final String WORK_MODE_NOT_FOUND = "Không tìm thấy chế độ làm việc";
    private static final String CREATE_WORK_MODE_SUCCESS = "Tạo chế độ làm việc thành công";
    private static final String UPDATE_WORK_MODE_SUCCESS = "Cập nhật chế độ làm việc thành công";
    private static final String DELETE_WORK_MODE_SUCCESS = "Xoá chế độ làm việc thành công";
    private static final String DELETE_WORK_MODE_FAIL = "Xoá chế độ làm việc thất bại";

    private final WorkModeRepository workModeRepository;
    private final WorkModeMapper workModeMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public ServiceResponse getAll(Boolean isAll) {
        List<OptionDTO> workModeDTOList;
        if (isAll) {
            List<WorkMode> workModes = this.workModeRepository.findAll();
            workModeDTOList = workModes.stream().map(workModeMapper::workModeToOptionDTO).toList();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_WORK_MODE_SUCCESS)
                    .data(Map.of("salary_ranges", workModeDTOList))
                    .build();
        }

        workModeDTOList = redisTemplate.opsForList().range("activeWorkModes", 0, -1);
        if (workModeDTOList == null || workModeDTOList.isEmpty()) {
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
        WorkMode workMode = this.workModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WORK_MODE_NOT_FOUND));

        OptionDTO workModeDTO = workModeMapper.workModeToOptionDTO(workMode);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_WORK_MODE_SUCCESS)
                .data(Map.of("work_mode", workModeDTO))
                .build();
    }

    @Override
    public ServiceResponse create(OptionDTO optionDTO) {
        WorkMode newWorkMode = this.workModeMapper.optionDTOToWorkMode(optionDTO);
        newWorkMode.setStatus(Status.ACTIVE);

        WorkMode workMode = this.workModeRepository.save(newWorkMode);
        OptionDTO workModeDTO = workModeMapper.workModeToOptionDTO(workMode);
        redisTemplate.opsForList().rightPush("activeWorkModes", workModeDTO);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED)
                .message(CREATE_WORK_MODE_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse update(OptionDTO object, Integer id) {
        WorkMode workMode = this.workModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WORK_MODE_NOT_FOUND));

        workModeMapper.updateWorkModeFromOptionDTO(object, workMode);
        redisTemplate.delete("activeWorkModes");

        this.workModeRepository.save(workMode);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(UPDATE_WORK_MODE_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse delete(Integer id) {
        WorkMode workMode = this.workModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WORK_MODE_NOT_FOUND + ", " + DELETE_WORK_MODE_FAIL));

        workMode.setStatus(Status.INACTIVE);
        this.workModeRepository.save(workMode);
        redisTemplate.delete("activeWorkModes");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(DELETE_WORK_MODE_SUCCESS)
                .build();
    }
}
