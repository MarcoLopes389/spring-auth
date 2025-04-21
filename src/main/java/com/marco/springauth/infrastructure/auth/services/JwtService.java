package com.marco.springauth.infrastructure.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.marco.springauth.infrastructure.auth.entities.UserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String SECRET = "sjbdcjdnvuho";
    private final String ISSUER = "springauth";

    public String generateToken(UserDetailsImpl userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create().withIssuer(ISSUER).sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.require(algorithm).withIssuer(ISSUER).build().verify(token).getSubject();
    }
}
