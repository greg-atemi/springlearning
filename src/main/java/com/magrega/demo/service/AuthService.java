package com.magrega.demo.service;

import com.magrega.demo.dto.user.LoginUserDTO;
import com.magrega.demo.dto.user.RegisterUserDTO;
import com.magrega.demo.dto.response.AuthResponse;
import com.magrega.demo.model.User;
import com.magrega.demo.model.enums.Role;
import com.magrega.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterUserDTO request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .build();
        user = userRepo.save(user);                    // save first, get ID back
        String token = jwtService.generateToken(user); // then generate token
        return AuthResponse.builder().accessToken(token).build();
    }

    public AuthResponse login(LoginUserDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        return AuthResponse.builder().accessToken(token).build();
    }
}