package com.edu.hcmute.service.category;


import com.edu.hcmute.entity.Location;
import com.edu.hcmute.repository.LocationRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private static final String GET_ALL_PROVINCE_SUCCESS = "Lấy danh sách tỉnh thành thành công";
    private static final String GET_ONE_PROVINCE_SUCCESS = "Lấy thông tin tỉnh thành thành công";

    private static final String NOT_FOUND_PROVINCE = "Không tìm thấy tỉnh thành";

    public ServiceResponse getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(GET_ALL_PROVINCE_SUCCESS)
                .data(Map.of("locations", locations))
                .build();
    }

    public ServiceResponse getOneLocation(Integer id) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND)
                    .status(ResponseDataStatus.ERROR)
                    .message(NOT_FOUND_PROVINCE)
                    .build();
        }
        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(GET_ONE_PROVINCE_SUCCESS)
                .data(Map.of("location", location))
                .build();
    }
}
