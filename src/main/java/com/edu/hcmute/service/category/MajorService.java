package com.edu.hcmute.service.category;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Major;
import com.edu.hcmute.exception.DuplicateEntryException;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.MajorMapper;
import com.edu.hcmute.repository.MajorRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.category.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MajorService implements GenericService<OptionDTO, Integer> {
    private static final String GET_ALL_MAJOR_SUCCESS = "Lấy danh sách ngành thành công";
    private static final String MAJOR_NOT_FOUND = "Không tìm thấy ngành";
    private static final String GET_ONE_MAJOR_SUCCESS = "Lấy thông tin ngành thành công";
    private static final String DELETE_MAJOR_FAIL = "Xoá ngành thất bại";
    private static final String DELETE_MAJOR_SUCCESS = "Xoá ngành thành công";
    private static final String DUPLICATE_MAJOR_NAME = "Tên ngành đã tồn tại";
    private static final String UPDATE_MAJOR_SUCCESS = "Cập nhật ngành thành công";
    private static final String CREATE_MAJOR_SUCCESS = "Tạo ngành mới thành công";

    private final MajorRepository majorRepository;
    private final RedisTemplate redisTemplate;
    private final MajorMapper majorMapper;


    @Override
    public ServiceResponse getAll(Boolean isAll) {
        if (isAll) {
            List<Major> majors = this.majorRepository.findAll();
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_MAJOR_SUCCESS)
                    .data(Map.of("majors", majors))
                    .build();
        }

        List<OptionDTO> foundMajorDTO = redisTemplate.opsForList().range("activeMajors", 0, -1);
        if (foundMajorDTO == null || foundMajorDTO.isEmpty()) {
            List<Major> foundMajors = this.majorRepository.findAllByStatus(Status.ACTIVE);
            foundMajorDTO = foundMajors.stream().map(majorMapper::majorToOptionDTO).toList();
            redisTemplate.opsForList().rightPushAll("activeMajors", foundMajorDTO);
        }

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_MAJOR_SUCCESS)
                .data(Map.of("majors", foundMajorDTO))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        Major major = this.majorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MAJOR_NOT_FOUND));

        OptionDTO majorDTO = majorMapper.majorToOptionDTO(major);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ONE_MAJOR_SUCCESS)
                .data(Map.of("major", majorDTO))
                .build();
    }

    @Override
    public ServiceResponse create(OptionDTO optionDTO) {
        Major major = majorRepository.findByName(optionDTO.getName()).orElse(null);
        if (major != null) {
            throw new DuplicateEntryException(DUPLICATE_MAJOR_NAME);
        }

        Major newMajor = majorMapper.optionDTOToMajor(optionDTO);
        newMajor.setStatus(Status.ACTIVE);

        majorRepository.save(newMajor);
        redisTemplate.delete("activeMajors");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED)
                .message(CREATE_MAJOR_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse update(OptionDTO optionDTO, Integer id) {
        Major major = majorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MAJOR_NOT_FOUND));

        majorMapper.updateMajorFromOptionDTO(optionDTO, major);
        majorRepository.save(major);
        redisTemplate.delete("activeMajors");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(UPDATE_MAJOR_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse delete(Integer id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MAJOR_NOT_FOUND + ", " + DELETE_MAJOR_FAIL));

        major.setStatus(Status.INACTIVE);
        majorRepository.save(major);
        redisTemplate.delete("activeMajors");

        return ServiceResponse.builder()
                .status(ResponseDataStatus.ERROR)
                .statusCode(HttpStatus.NOT_FOUND)
                .message(DELETE_MAJOR_SUCCESS)
                .build();
    }
}
