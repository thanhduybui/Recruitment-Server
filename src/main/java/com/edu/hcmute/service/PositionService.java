package com.edu.hcmute.service;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.PositionDTO;
import com.edu.hcmute.entity.Position;
import com.edu.hcmute.repository.PositionRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PositionService implements GenericService<PositionDTO, Integer> {
    private static final String GET_ALL_POSITION_SUCCESS = "Lấy danh sách vị trí thành công";
    private static final String NOT_FOUND_POSITION = "Không tìm thấy vị trí";
    private static final String DELETE_POSITION_FAIL = "Xoá vị trí thất bại";
    private static final String CREATE_POSITION_SUCCESS = "Tạo vị trí mới thành công";
    private static final String DELETE_POSITION_SUCCESS = "Xoá vị trí thành công";
    private final PositionRepository positionRepository;

    @Override
    public ServiceResponse getAll() {
        List<Position> positions = this.positionRepository.findAllByStatus(Status.ACTIVE);
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_POSITION_SUCCESS)
                .data(Map.of("positions", positions))
                .build();
    }

    @Override
    public ServiceResponse getOne(Integer id) {
        Position position = this.positionRepository.findById(id).orElse(null);
        if (position == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.ERROR)
                    .statusCode(HttpStatus.NOT_FOUND)
                    .message(NOT_FOUND_POSITION)
                    .build();
        }

        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(GET_ALL_POSITION_SUCCESS)
                .data(Map.of("position", position))
                .build();
    }

    @Override
    public ServiceResponse create(PositionDTO positionDTO) {
        Position position = Position.builder()
                .name(positionDTO.getName())
                .description(positionDTO.getDescription())
                .status(Status.ACTIVE)
                .build();

        positionRepository.save(position);
        return ServiceResponse.builder()
                .status(ResponseDataStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED)
                .message(CREATE_POSITION_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse update(PositionDTO positionDTO) {
        return null;
    }

    @Override
    public ServiceResponse delete(Integer id) {
        Position position = positionRepository.findById(id).orElse(null);

        if (position == null) {
            return ServiceResponse.builder()
                    .status(ResponseDataStatus.SUCCESS)
                    .statusCode(HttpStatus.OK)
                    .message(NOT_FOUND_POSITION + ", " + DELETE_POSITION_FAIL)
                    .build();
        }

        position.setStatus(Status.INACTIVE);
        positionRepository.save(position);

        return ServiceResponse.builder()
                .status(ResponseDataStatus.ERROR)
                .statusCode(HttpStatus.NOT_FOUND)
                .message(DELETE_POSITION_SUCCESS)
                .build();

    }
}
