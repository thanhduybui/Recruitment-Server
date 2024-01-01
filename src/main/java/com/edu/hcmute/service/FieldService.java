package com.edu.hcmute.service;

import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Field;
import com.edu.hcmute.mapper.FieldMapper;
import com.edu.hcmute.repository.FieldRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class FieldService implements GenericService<OptionDTO, Integer> {
    private static final String GET_ALL_FIELD_SUCCESS = "Lấy danh sách lĩnh vực thành công";
    private static final String GET_ONE_SUCCESS = "Lấy thông tin lĩnh vực thành công";
    private static final String NOT_FOUND_FIELD = "Không tìm thấy lĩnh vực";
    private static final String DELETE_FIELD_FAIL = "Xoá lĩnh vực thất bại";
    private static final String DELETE_FIELD_SUCCESS = "Xoá lĩnh vực thành công";
    private final FieldRepository fieldRepository;
    private final RedisTemplate redisTemplate;
    private final FieldMapper fieldMapper;

    @Override
    public ServiceResponse getAll(Boolean isAll) {
        if (isAll) {
            List<Field> fields = this.fieldRepository.findAll();
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_FIELD_SUCCESS)
                    .data(Map.of("fields", fields))
                    .build();
        }

        List<OptionDTO> foundFieldDTO = redisTemplate.opsForList().range("activeFields", 0, -1);
        if (foundFieldDTO == null || foundFieldDTO.isEmpty()) {
            List<Field> foundFields = this.fieldRepository.findAllByStatus(Status.ACTIVE);
            foundFieldDTO = foundFields.stream().map(fieldMapper::fieldToOptionDTO).toList();
            redisTemplate.opsForList().rightPushAll("activeFields", foundFieldDTO);
        }

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_FIELD_SUCCESS)
                .data(Map.of("fields", foundFieldDTO))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        Field field = this.fieldRepository.findById(id).orElse(null);
        if (field == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .message(NOT_FOUND_FIELD)
                    .build();
        }

        OptionDTO fieldDTO = fieldMapper.fieldToOptionDTO(field);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ONE_SUCCESS)
                .data(Map.of("field", fieldDTO))
                .build();
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
        Field field = fieldRepository.findById(id).orElse(null);

        if (field == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(NOT_FOUND_FIELD + ", " + DELETE_FIELD_FAIL)
                    .build();
        }

        field.setStatus(Status.INACTIVE);
        fieldRepository.save(field);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.ERROR)
                .statusCode(HttpStatus.NOT_FOUND)
                .message(DELETE_FIELD_SUCCESS)
                .build();
    }
}
