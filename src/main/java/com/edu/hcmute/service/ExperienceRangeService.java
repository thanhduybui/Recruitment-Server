package com.edu.hcmute.service;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.ExperienceRange;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.ExperienceRangeMapper;
import com.edu.hcmute.repository.ExperienceRangeRepository;
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
public class ExperienceRangeService implements GenericService<OptionDTO, Integer> {
    private static final String GET_ALL_EXPERIENCE_SUCCESS = "Lấy danh sách kinh nghiệm thành công";
    private static final String NOT_FOUND_EXPERIENCE = "Không tìm thấy kinh nghiệm";
    private static final String GET_ONE_EXPERIENCE_SUCCESS = "Lấy thông tin kinh nghiệm thành công";
    private static final String DELETE_EXPERIENCE_SUCCESS = "Xoá kinh nghiệm thành công";
    private final ExperienceRangeRepository expRangeRepository;
    private final ExperienceRangeMapper expRangeMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public ServiceResponse getAll(Boolean isAll) {
        List<OptionDTO> experienceRangeDTOList;
        if (isAll) {
            List<ExperienceRange> experienceRanges = this.expRangeRepository.findAll();
            experienceRangeDTOList = experienceRanges.stream().map(expRangeMapper::
                    experienceRangeToOptionDTO).toList();
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_EXPERIENCE_SUCCESS)
                    .data(Map.of("experience_ranges", experienceRangeDTOList))
                    .build();
        }

        experienceRangeDTOList = redisTemplate.opsForList().range("activeExpRanges", 0, -1);
        if (experienceRangeDTOList == null || experienceRangeDTOList.isEmpty()) {
            List<ExperienceRange> foundSalaryRanges = this.expRangeRepository.findAllByStatus(Status.ACTIVE);
            experienceRangeDTOList = foundSalaryRanges.stream().map(expRangeMapper::experienceRangeToOptionDTO).toList();
            redisTemplate.opsForList().rightPushAll("activeExpRanges", experienceRangeDTOList);
        }
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_EXPERIENCE_SUCCESS)
                .data(Map.of("experience_ranges", experienceRangeDTOList))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        ExperienceRange foundExperienceRange = this.expRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXPERIENCE));

        OptionDTO foundExperienceRangeDTO = expRangeMapper.experienceRangeToOptionDTO(foundExperienceRange);
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ONE_EXPERIENCE_SUCCESS)
                .data(Map.of("experience_range", foundExperienceRangeDTO))
                .build();
    }

    @Override
    public ServiceResponse create(OptionDTO optionDTO) {

        ExperienceRange experienceRange = expRangeMapper.optionDTOToExperienceRange(optionDTO);
        this.expRangeRepository.save(experienceRange);
        redisTemplate.delete("activeExpRanges");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ONE_EXPERIENCE_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse update(OptionDTO optionDTO, Integer id) {

        ExperienceRange foundExperienceRange = this.expRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXPERIENCE));

        expRangeMapper.updateExperienceRangeFromOptionDTO(optionDTO, foundExperienceRange);
        this.expRangeRepository.save(foundExperienceRange);
        redisTemplate.delete("activeExpRanges");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ONE_EXPERIENCE_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse delete(Integer id) {
        ExperienceRange foundExperienceRange = this.expRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXPERIENCE));
        foundExperienceRange.setStatus(Status.INACTIVE);
        this.expRangeRepository.save(foundExperienceRange);

        redisTemplate.delete("activeExpRanges");
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(DELETE_EXPERIENCE_SUCCESS)
                .build();
    }
}
