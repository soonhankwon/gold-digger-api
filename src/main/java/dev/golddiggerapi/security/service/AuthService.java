package dev.golddiggerapi.security.service;

import dev.golddiggerapi.security.jwt.JwtProvider;
import dev.golddiggerapi.security.repository.RedisTokenRepository;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RedisTokenRepository redisTokenRepository;

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        User user = verifyRefreshTokenExists(request);
        String username = user.getUsername();

        String newAccessToken = jwtProvider.generateAccessToken(user.getId(), username);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

        String newRefreshToken = jwtProvider.generateRefreshToken(username);
        redisTokenRepository.saveRefreshToken(newRefreshToken, username);
        response.addHeader("Refresh", newRefreshToken);
    }

    private User verifyRefreshTokenExists(HttpServletRequest request) {
        String refreshToken = getRefreshTokenByHeader(request);
        User user = getUserByRefreshToken(refreshToken);
        verifyRedis(user, refreshToken);
        redisTokenRepository.deleteRefreshToken(refreshToken);
        return user;
    }

    private String getRefreshTokenByHeader(HttpServletRequest request) {
        String refresh = request.getHeader("Refresh");
        return Optional.ofNullable(refresh)
                .orElseThrow(() -> new RuntimeException("Cookie not found!"));
    }

    private User getUserByRefreshToken(String refreshToken) {
        String username = jwtProvider.getClaims(refreshToken).getSubject();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("RefreshToken not found in Database!"));
    }

    private void verifyRedis(User user, String refreshToken) {
        String subject = Optional.ofNullable(redisTokenRepository.getSubject(refreshToken))
                .orElseThrow(() -> new RuntimeException("RefreshToken not found in redis!"));
        if (!user.getUsername().equals(subject)) {
            throw new RuntimeException("RefreshToken Subject is not matched!");
        }
    }
}
