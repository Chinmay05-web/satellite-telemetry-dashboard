
package com.satellite.dashboard.controller;

import com.satellite.dashboard.dto.AuthResponse;
import com.satellite.dashboard.dto.LoginRequest;
import com.satellite.dashboard.dto.RegisterRequest;
import com.satellite.dashboard.model.AppUser;
import com.satellite.dashboard.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.satellite.dashboard.dto.AuthResponse;
import com.satellite.dashboard.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public AuthResponse login(
        @Valid @RequestBody LoginRequest request) {

    return authService.login(request);
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }
}
