package com.mate.academy.demo.security;

import com.mate.academy.demo.exception.DataProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey secret;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);

            return claimsJws.getPayload().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new DataProcessingException("Wrong JWT key or token expired");
        }
    }

    public String getUserName(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsTFunction.apply(claims);
    }
}
