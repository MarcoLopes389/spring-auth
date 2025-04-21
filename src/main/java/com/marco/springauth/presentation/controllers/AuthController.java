package com.marco.springauth.presentation.controllers;

import com.marco.springauth.infrastructure.auth.entities.UserDetailsImpl;
import com.marco.springauth.infrastructure.auth.services.JwtService;
import com.marco.springauth.presentation.dtos.LoginRequestDto;
import com.marco.springauth.presentation.dtos.LoginResponseDto;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto body) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        var auth = authenticationManager.authenticate(authenticationToken);
        var token = jwtService.generateToken((UserDetailsImpl) auth);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
