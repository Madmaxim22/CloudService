package com.example.cloudservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // метод извлечения логина
    public String extractUsername(String token) {
        return null;
    }

    // получение всех параметров из токена
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                // установка ключа подписи
                .setSigningKey(getSingKey())
                .build()
                // как только строительство объекта завершается
                .parseClaimsJws(token)
                .getBody();
    }

    // получение ключа подписи декодируя секретный ключ
    private Key getSingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
