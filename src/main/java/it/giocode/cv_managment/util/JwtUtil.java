package it.giocode.cv_managment.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "wjlDdL9R2ED0gFYjJ40V1e+Wz7XR3IjdBSbdrz9Me38=";
    private static final int EXPIRATION_TIME = 3600000;

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email, Long userId, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractionClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("Claims: " + claims);
            return claims;
        }catch (JwtException | IllegalArgumentException e) {
            System.out.println("Errore durante il parsing del token: " + e.getMessage());
            return null;
        }
    }

    public String validationToken(String token) {
        Claims claims = extractionClaims(token);
        return (claims != null) ? claims.getSubject() : null;
    }

    public String getRoleFromToken(String token) {
        Claims claims = extractionClaims(token);
        return (claims != null) ? claims.get("role", String.class) : null;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = extractionClaims(token);
        return (claims != null) ? claims.get("id", Long.class) : null;
    }
}
