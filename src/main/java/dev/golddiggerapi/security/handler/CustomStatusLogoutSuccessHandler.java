package dev.golddiggerapi.security.handler;

import dev.golddiggerapi.security.repository.RedisTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomStatusLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisTokenRepository redisTokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String refreshToken = request.getHeader("Refresh");
        redisTokenRepository.deleteRefreshToken(refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }
}
