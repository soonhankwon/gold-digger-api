package dev.golddiggerapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access token expired!"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token!"),
    INFORMATION_NOT_MATCHED(HttpStatus.CONFLICT, "Information not matched!"),
    LOGIN_REQUIRED_FIRST(HttpStatus.UNAUTHORIZED, "Login required first"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username already exists"),
    USER_NOT_FOUND_DB(HttpStatus.NOT_FOUND, "User not found in db"),
    CATEGORY_NOT_FOUND_DB(HttpStatus.NOT_FOUND, "Category not found in db"),
    NOT_USER_EXPENDITURE_ID(HttpStatus.BAD_REQUEST, "This is not user's expenditure id"),
    DUPLICATED_USER_BUDGET(HttpStatus.CONFLICT, "Duplicated user budget exists");


    private final HttpStatus status;

    private final String message;
}
