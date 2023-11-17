package dev.golddiggerapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access token expired!"),
    CATEGORY_NOT_FOUND_DB(HttpStatus.NOT_FOUND, "Category not found in db"),
    DUPLICATED_USER_BUDGET(HttpStatus.CONFLICT, "Duplicated user budget exists"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token!"),
    INFORMATION_NOT_MATCHED(HttpStatus.CONFLICT, "Information not matched!"),
    LOGIN_REQUIRED_FIRST(HttpStatus.UNAUTHORIZED, "Login required first"),
    NOT_USER_EXPENDITURE_ID(HttpStatus.BAD_REQUEST, "This is not user's expenditure id"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username already exists"),
    USER_NOT_FOUND_DB(HttpStatus.NOT_FOUND, "User not found in db"),
    USER_BUDGET_NOT_FOUND_DB(HttpStatus.NOT_FOUND, "User budget not found in db"),
    INVALID_PARAMETER_DATE_NONE_SERVICE_DAY(HttpStatus.BAD_REQUEST, "Parameter's date includes none service day"),
    INVALID_PARAMETER_START_DATE(HttpStatus.BAD_REQUEST, "Parameter's start date can't after end"),
    INVALID_EXPENDITURES_GET_DURATION(HttpStatus.BAD_REQUEST, "Get Expenditures duration can't up to 30days"),
    INVALID_PARAMETER_YEAR_BEFORE_NOW(HttpStatus.BAD_REQUEST, "Year can't before now");

    private final HttpStatus status;

    private final String message;
}
