package com.edu.hcmute.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


    @PostMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Hello");
    }
}
