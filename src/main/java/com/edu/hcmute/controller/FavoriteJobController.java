package com.edu.hcmute.controller;


import com.edu.hcmute.dto.FavoriteJobDTO;
import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.FavoriteJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorite-jobs")
@RequiredArgsConstructor
public class FavoriteJobController {

    private final FavoriteJobService favoriteJobService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CANDIDATE')")
    public ResponseEntity<ResponseData>  addFavoriteJob(@RequestBody @Valid FavoriteJobDTO favoriteJobDTO) {
        ServiceResponse serviceResponse = favoriteJobService.addFavoriteJobToList(favoriteJobDTO);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }
}
