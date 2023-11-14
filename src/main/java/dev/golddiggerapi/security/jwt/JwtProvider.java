package dev.golddiggerapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Map<String, Object> createClaims(Long id, String username) {
        return Map.of("id", id, "username", username);
    }

    public String generateAccessToken(Long id, String username) {
        Instant now = Instant.now();
        Map<String, Object> claims = createClaims(id, username);
        return Jwts.builder()
                .issuer("gold")
                .subject(username)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(3, ChronoUnit.HOURS)))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer("gold")
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(72, ChronoUnit.HOURS)))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
