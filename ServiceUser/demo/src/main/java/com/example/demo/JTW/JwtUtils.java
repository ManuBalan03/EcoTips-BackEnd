package com.example.demo.JTW;
import com.example.demo.Service.UserDetailsImpl;
import com.example.demo.Service.UserDetailsServiceImpl;
import com.example.demo.Service.UserServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtils {
    private final String jwtSecret = "ecoTipsClaveSuperSeguraYlarga20030409"; // mínimo 256 bits
    private final long jwtExpirationMs = 86400000; // 24 horas

    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());


    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // el email
                .claim("userId", userPrincipal.getId())   // ✅ aquí sí usas getId()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
