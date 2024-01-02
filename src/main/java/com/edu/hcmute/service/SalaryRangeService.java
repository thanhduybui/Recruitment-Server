package com.edu.hcmute.service;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.SalaryRange;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.SalaryRangeMapper;
import com.edu.hcmute.repository.SalaryRangeRepository;
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
public class SalaryRangeService implements GenericService<OptionDTO, Integer>{
    private final SalaryRangeRepository salaryRangeRepository;
    private final SalaryRangeMapper salaryRangeMapper;
    private final RedisTemplate redisTemplate;
    private static final String NOT_FOUND_SALARY = "Không tìm thấy mức lương";
    private static final String GET_ONE_SALARY_SUCCESS = "Lấy mức lương thành công";
    private static final String GET_ALL_SALARY_SUCCESS = "Lấy danh sách mức lương thành công";
    private static final String DELETE_SALARY_SUCCESS = "Xoá mức lương thành công";
    private static final String UPDATE_SALARY_SUCCESS = "Cập nhật mức lương thành công";
    private static final String CREATE_SALARY_SUCCESS = "Tạo mức lương thành công";

    @Override
    public ServiceResponse getAll(Boolean isAll) {
        List<OptionDTO> salaryRangeDTOList;
        if (isAll) {
            List<SalaryRange> salaryRanges = this.salaryRangeRepository.findAll();
           salaryRangeDTOList = salaryRanges.stream().map(salaryRangeMapper::salaryRangeToOptionDTO).toList();

            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_SALARY_SUCCESS)
                    .data(Map.of("salary_ranges", salaryRangeDTOList))
                    .build();
        }

        salaryRangeDTOList = redisTemplate.opsForList().range("activeSalaryRanges", 0, -1);
        if (salaryRangeDTOList  == null || salaryRangeDTOList .isEmpty()) {
            List<SalaryRange> foundSalaryRanges = this.salaryRangeRepository.findAllByStatus(Status.ACTIVE);
            salaryRangeDTOList  = foundSalaryRanges.stream().map(salaryRangeMapper::salaryRangeToOptionDTO).toList();
            redisTemplate.opsForList().rightPushAll("activeSalaryRanges", salaryRangeDTOList );
        }

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_SALARY_SUCCESS)
                .data(Map.of("salary_ranges", salaryRangeDTOList ))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        SalaryRange salaryRange = this.salaryRangeRepository.findById(id).orElse(null);
        if (salaryRange == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .message(NOT_FOUND_SALARY)
                    .build();
        }

        OptionDTO salaryRangeDTO = salaryRangeMapper.salaryRangeToOptionDTO(salaryRange);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ONE_SALARY_SUCCESS)
                .data(Map.of("salary_range", salaryRangeDTO))
                .build();
    }

    @Override
    public ServiceResponse create(OptionDTO object) {

        SalaryRange salaryRange = salaryRangeMapper.optionDTOToSalaryRange(object);
        salaryRange.setStatus(Status.ACTIVE);
        this.salaryRangeRepository.save(salaryRange);
        redisTemplate.delete("activeSalaryRanges");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(CREATE_SALARY_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse update(OptionDTO optionDTO, Integer id) {
        SalaryRange foundSalaryRange = this.salaryRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_SALARY));

        salaryRangeMapper.updateSalaryRangeFromOptionDTO(optionDTO, foundSalaryRange);
        this.salaryRangeRepository.save(foundSalaryRange);
        redisTemplate.delete("activeSalaryRanges");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(UPDATE_SALARY_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse delete(Integer id) {
        SalaryRange foundSalaryRange = this.salaryRangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_SALARY ));

        foundSalaryRange.setStatus(Status.INACTIVE);
        this.salaryRangeRepository.save(foundSalaryRange);
        redisTemplate.delete("activeSalaryRanges");
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(DELETE_SALARY_SUCCESS)
                .build();
    }
}
