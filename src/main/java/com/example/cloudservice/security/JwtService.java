package com.example.cloudservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // метод извлечения логина
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // универсальный метод извлечения параметра
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Objects> extractClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extractClaims)
                // username -> email
                .setSubject(userDetails.getUsername())
                // дата создания токена
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // дата истечения срока токена
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSingKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // проверяем что емайл в токене такой же, как у пользователя
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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
