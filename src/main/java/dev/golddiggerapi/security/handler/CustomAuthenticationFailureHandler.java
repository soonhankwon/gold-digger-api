package dev.golddiggerapi.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.golddiggerapi.exception.ErrorResponse;
import dev.golddiggerapi.exception.CustomErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        CustomErrorCode customErrorCode = CustomErrorCode.INFORMATION_NOT_MATCHED;
        ErrorResponse errorResponse = ErrorResponse.of(customErrorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(customErrorCode.getStatus().value());
    }
}
