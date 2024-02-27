package com.edu.hcmute.service;


import com.edu.hcmute.entity.Location;
import com.edu.hcmute.repository.LocationRepository;
import com.edu.hcmute.response.ResponseDataStatus;
import com.edu.hcmute.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private static final String GET_ALL_PROVINCE_SUCCESS = "Lấy danh sách tỉnh thành thành công";

    public ServiceResponse getAllProvince() {
        List<Location> locations = locationRepository.findAll();
        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataStatus.SUCCESS)
                .message(GET_ALL_PROVINCE_SUCCESS)
                .data(locations)
                .build();
    }
}
