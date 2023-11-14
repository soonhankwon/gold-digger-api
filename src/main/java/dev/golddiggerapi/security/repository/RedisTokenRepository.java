package dev.golddiggerapi.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(String refreshToken, String username) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(refreshToken, username, Duration.ofMinutes(60 * 72));
    }

    public String getSubject(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
