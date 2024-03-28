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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite-jobs")
@RequiredArgsConstructor
public class FavoriteJobController {

    private final FavoriteJobService favoriteJobService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CANDIDATE')")
    public ResponseEntity<ResponseData> addFavoriteJob(@RequestBody @Valid FavoriteJobDTO favoriteJobDTO) {
        ServiceResponse serviceResponse = favoriteJobService.addFavoriteJobToList(favoriteJobDTO);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('CANDIDATE')")
    public ResponseEntity<ResponseData> getAllByUser(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        ServiceResponse serviceResponse = favoriteJobService.getAllByUser(page, size);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());

    }
}
