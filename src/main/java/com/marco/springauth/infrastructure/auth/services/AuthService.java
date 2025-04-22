package com.marco.springauth.infrastructure.auth.services;

import com.marco.springauth.domain.entities.User;
import com.marco.springauth.infrastructure.auth.entities.UserDetailsImpl;
import com.marco.springauth.infrastructure.repositories.UserRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(String email, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        var auth = authenticationManager.authenticate(authenticationToken);

        return jwtService.generateToken((UserDetailsImpl) auth.getPrincipal());
    }

    public User register(String email, String password) throws AuthenticationException {
        var existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new AuthenticationException("Usuário já existe para este email");
        }

        var encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword);
        return userRepository.save(user);
    }
}
