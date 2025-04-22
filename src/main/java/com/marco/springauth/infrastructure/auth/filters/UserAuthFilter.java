package com.marco.springauth.infrastructure.auth.filters;

import com.marco.springauth.infrastructure.auth.entities.UserDetailsImpl;
import com.marco.springauth.infrastructure.auth.exceptions.AuthenticationException;
import com.marco.springauth.infrastructure.auth.services.JwtService;
import com.marco.springauth.infrastructure.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UserAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoveryTokenFromRequest(request);

        if (!token.isEmpty()) {
            var subject = jwtService.getSubjectFromToken(token);
            var user = userRepository.findByEmail(subject);

            if (user.isPresent()) {
                UserDetailsImpl userDetails = new UserDetailsImpl(user.get());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoveryTokenFromRequest(HttpServletRequest request) {
        var authenticationHeader = request.getHeader("Authorization");

        if (authenticationHeader == null || authenticationHeader.isEmpty()) {
            return "";
        }

        return authenticationHeader.replace("Bearer", "");
    }
}
