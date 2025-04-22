package com.marco.springauth.presentation.controllers;

import com.marco.springauth.infrastructure.auth.entities.UserDetailsImpl;
import com.marco.springauth.infrastructure.auth.services.AuthService;
import com.marco.springauth.infrastructure.auth.services.JwtService;
import com.marco.springauth.presentation.dtos.LoginRequestDto;
import com.marco.springauth.presentation.dtos.LoginResponseDto;
import com.marco.springauth.presentation.dtos.RegisterUserRequestDto;
import com.marco.springauth.presentation.dtos.RegisterUserResponseDto;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto body) {
        var token = authService.login(body.email(), body.password());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("register")
    public ResponseEntity<RegisterUserResponseDto> register(@RequestBody RegisterUserRequestDto body) throws AuthenticationException {
        var user = authService.register(body.email(), body.password());

        return ResponseEntity.ok(new RegisterUserResponseDto(user.getId(), user.getEmail()));
    }
}
