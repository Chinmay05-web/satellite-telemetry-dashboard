
package com.satellite.dashboard.service;

import com.satellite.dashboard.dto.RegisterRequest;
import com.satellite.dashboard.model.AppUser;
import com.satellite.dashboard.repository.AppUserRepository;
import com.satellite.dashboard.exception.DuplicateEmailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.satellite.dashboard.dto.AuthResponse;
import com.satellite.dashboard.dto.LoginRequest;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
        AppUserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    }

    public AppUser register(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
        throw new DuplicateEmailException(
                "An account with this email already exists");
        }

        AppUser user = new AppUser();

        user.setName(request.getName().trim());
        user.setEmail(email);

        // Never store the original password.
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
    
    public AuthResponse login(LoginRequest request) {

    String email = request.getEmail()
            .trim()
            .toLowerCase(java.util.Locale.ROOT);

    AppUser user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new BadCredentialsException("Invalid email or password"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash())) {

        throw new BadCredentialsException(
                "Invalid email or password");
    }

    String token = jwtService.generateToken(user);

    return new AuthResponse(
            token,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
    );
    }
}
