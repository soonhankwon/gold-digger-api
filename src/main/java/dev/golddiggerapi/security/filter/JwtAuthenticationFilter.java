package dev.golddiggerapi.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.golddiggerapi.security.dto.UsernamePassword;
import dev.golddiggerapi.security.jwt.JwtProvider;
import dev.golddiggerapi.security.principal.UserPrincipal;
import dev.golddiggerapi.security.repository.RedisTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final RedisTokenRepository redisTokenRepository;


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        UsernamePassword usernamePassword = objectMapper.readValue(request.getInputStream(), UsernamePassword.class);
        var authentication = new UsernamePasswordAuthenticationToken(usernamePassword.getUsername(), usernamePassword.getPassword());
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        String username = principal.getUsername();
        String accessToken = jwtProvider.generateAccessToken(principal.getId(), username);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        String refreshToken = jwtProvider.generateRefreshToken(username);
        redisTokenRepository.saveRefreshToken(refreshToken, username);
        response.setHeader("Refresh", "Bearer " + refreshToken);
    }
}
