package dev.golddiggerapi.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.golddiggerapi.security.filter.JwtAuthenticationFilter;
import dev.golddiggerapi.security.filter.JwtVerificationFilter;
import dev.golddiggerapi.security.jwt.JwtProvider;
import dev.golddiggerapi.security.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFilterDsl extends AbstractHttpConfigurer<JwtFilterDsl, HttpSecurity> {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;
    private final RedisTokenRepository redisTokenRepository;

    @Override
    public void configure(HttpSecurity security) {
        AuthenticationManager authenticationManager = security.getSharedObject(AuthenticationManager.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider, objectMapper, redisTokenRepository);
        jwtAuthenticationFilter.setFilterProcessesUrl("/sign-in");
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        security.addFilter(jwtAuthenticationFilter);

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtProvider);
        security.addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }
}
