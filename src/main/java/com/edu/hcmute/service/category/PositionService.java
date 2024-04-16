package com.edu.hcmute.service.category;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.entity.Position;
import com.edu.hcmute.exception.DuplicateEntryException;
import com.edu.hcmute.exception.ResourceNotFoundException;
import com.edu.hcmute.mapper.PositionMapper;
import com.edu.hcmute.repository.PositionRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionService implements GenericService<OptionDTO, Integer> {
    private static final String GET_ALL_POSITION_SUCCESS = "Lấy danh sách vị trí thành công";
    private static final String NOT_FOUND_POSITION = "Không tìm thấy vị trí";
    private static final String DELETE_POSITION_FAIL = "Xoá vị trí thất bại";
    private static final String CREATE_POSITION_SUCCESS = "Tạo vị trí mới thành công";
    private static final String DELETE_POSITION_SUCCESS = "Xoá vị trí thành công";
    private static final String UPDATE_POSITION_SUCCESS = "Cập nhật vị trí thành công";
    private static final String CREATE_POSITION_FAIL = "Tao vị trí mới thất bại";
    private static final String DUPLICATE_POSITION_NAME = "Tên vị trí đã tồn tại";

    private final PositionRepository positionRepository;
    private final RedisTemplate redisTemplate;
    private final PositionMapper positionMapper;

    @Override
    public ServiceResponse getAll(Boolean isAll) {
        List<OptionDTO> foundPositionDTOList;
        if (isAll) {
            List<Position> positions = this.positionRepository.findAll();
            foundPositionDTOList = positions.stream().map(positionMapper::positionToOptionDTO).toList();
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(GET_ALL_POSITION_SUCCESS)
                    .data(Map.of("positions", foundPositionDTOList))
                    .build();
        }

        foundPositionDTOList = redisTemplate.opsForList().range("activePositions", 0, -1);
        if (foundPositionDTOList == null || foundPositionDTOList.isEmpty()) {
            List<Position> foundPositions = this.positionRepository.findAllByStatus(Status.ACTIVE);
            foundPositionDTOList = foundPositions.stream().map(positionMapper::positionToOptionDTO).toList();
            redisTemplate.opsForList().rightPushAll("activePositions", foundPositionDTOList);
        }

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_POSITION_SUCCESS)
                .data(Map.of("positions", foundPositionDTOList))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        Position position = this.positionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_POSITION));

        OptionDTO positionDTO = positionMapper.positionToOptionDTO(position);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_POSITION_SUCCESS)
                .data(Map.of("position", positionDTO))
                .build();
    }

    @Override
    public ServiceResponse create(OptionDTO optionDTO) {
        Position position = positionRepository.findByName(optionDTO.getName()).orElse(null);

        if (position != null) {
            throw new DuplicateEntryException(DUPLICATE_POSITION_NAME);
        }

        Position newPosition = Position.builder()
                .name(optionDTO.getName())
                .description(optionDTO.getDescription())
                .status(Status.ACTIVE)
                .build();

        positionRepository.save(newPosition);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED)
                .message(CREATE_POSITION_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse update(OptionDTO optionDTO, Integer id) {
        Position position = positionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_POSITION));
        redisTemplate.delete("activePositions");
        positionMapper.updatePositionFromOptionDTO(optionDTO, position);
        positionRepository.save(position);
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(UPDATE_POSITION_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse delete(Integer id) {
        Position position = positionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_POSITION + ", " + DELETE_POSITION_FAIL));

        redisTemplate.delete("activePositions");
        position.setStatus(Status.INACTIVE);
        positionRepository.save(position);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.ERROR)
                .statusCode(HttpStatus.NOT_FOUND)
                .message(DELETE_POSITION_SUCCESS)
                .build();
    }
}
