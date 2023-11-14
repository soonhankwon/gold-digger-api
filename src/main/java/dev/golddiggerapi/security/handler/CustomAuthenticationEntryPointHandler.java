package dev.golddiggerapi.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.golddiggerapi.exception.ErrorResponse;
import dev.golddiggerapi.exception.CustomErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Object e = request.getAttribute("exception");
        if (!isExceptionCode(e)) {
            responseWithJson(response, CustomErrorCode.LOGIN_REQUIRED_FIRST);
            return;
        }
        CustomErrorCode customErrorCode = convertToExceptionCode(e);
        responseWithJson(response, customErrorCode);
    }

    private boolean isExceptionCode(Object exception) {
        return exception instanceof CustomErrorCode;
    }

    private CustomErrorCode convertToExceptionCode(Object object) {
        return (CustomErrorCode) object;
    }

    private void responseWithJson(HttpServletResponse response, CustomErrorCode customErrorCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(customErrorCode.getStatus().value());
        response.getWriter().write(toJsonResponse(customErrorCode));
    }

    private String toJsonResponse(CustomErrorCode customErrorCode) throws JsonProcessingException {
        ErrorResponse errorResponse = ErrorResponse.of(customErrorCode);
        return objectMapper.writeValueAsString(errorResponse);
    }
}
