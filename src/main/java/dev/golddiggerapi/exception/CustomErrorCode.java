package dev.golddiggerapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    ACCESS_TOKEN_EXPIRED("AU01", HttpStatus.UNAUTHORIZED, "Access token expired!"),
    INVALID_TOKEN("AU02", HttpStatus.UNAUTHORIZED, "Invalid Token!"),

    INFORMATION_NOT_MATCHED("AU03", HttpStatus.CONFLICT, "Information not matched!"),
    LOGIN_REQUIRED_FIRST("AU04", HttpStatus.UNAUTHORIZED, "Login required first");

    private final String errorCode;

    private final HttpStatus status;

    private final String message;
}
