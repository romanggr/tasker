package org.example.tasker_back.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${auth.app.secret}")
    private String secretKey;

    @Value("${auth.app.lifeTime}")
    private long jwtExpiration;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateToken(String email, long expiration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isTokenValid(String token, String email) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(2)
                .parseClaimsJws(token)
                .getBody();

        boolean isExpired = claims.getExpiration().before(new Date());
        boolean isEmailValid = claims.getSubject().equals(email);

        return !isExpired && isEmailValid;
    }
}
