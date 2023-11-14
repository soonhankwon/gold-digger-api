package dev.golddiggerapi.security.filter;

import dev.golddiggerapi.exception.CustomErrorCode;
import dev.golddiggerapi.security.jwt.JwtProvider;
import dev.golddiggerapi.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String bearerToken = getAuthenticationHeaderValue(request);
        return bearerToken == null || !bearerToken.startsWith("Bearer ");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            setAuthenticationToContext(request);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", CustomErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (SignatureException e) {
            request.setAttribute("exception", CustomErrorCode.INVALID_TOKEN);
        } catch (JwtException e) {
            log.warn("JwtException: {}", e.getClass());
            request.setAttribute("exception", CustomErrorCode.INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(HttpServletRequest request) {
        Authentication authentication = createAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(HttpServletRequest request) {
        String accessToken = getAuthenticationHeaderValue(request).substring("Bearer ".length());
        Claims claims = jwtProvider.getClaims(accessToken);
        UserPrincipal userPrincipal = new UserPrincipal(claims);
        return new UsernamePasswordAuthenticationToken(userPrincipal, "", userPrincipal.getAuthorities());
    }

    private String getAuthenticationHeaderValue(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
