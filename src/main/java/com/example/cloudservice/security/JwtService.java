package com.example.cloudservice.security;

import com.example.cloudservice.model.token.Token;
import com.example.cloudservice.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    private final TokenRepository tokenRepository;

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
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSingKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // проверяем что емайл в токене такой же, как у пользователя
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        if(extractExpiration(token).before(new Date())) {
            Token saveToken = tokenRepository.findByToken(token).get();
            saveToken.setExpired(true);
            tokenRepository.save(saveToken);
            return true;
        }
        return false;
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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
