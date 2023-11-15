package dev.golddiggerapi.exception.detail;

import dev.golddiggerapi.exception.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ApiException extends RuntimeException {

    private final CustomErrorCode customErrorCode;
}
