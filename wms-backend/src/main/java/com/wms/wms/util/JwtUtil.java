package com.wms.wms.util;

import java.security.Key;

import org.springframework.stereotype.Component;

import com.wms.wms.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public String generateToken(User user) {
	    return Jwts.builder()
	            .setSubject(user.getUsername())
	            .claim("role", user.getRole().name()) // ✅ ADD THIS
	            .signWith(key)
	            .compact();
	}

	public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

	public String extractRole(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .get("role", String.class);
	}
}
