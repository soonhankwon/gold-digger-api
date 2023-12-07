package dev.golddiggerapi.global.aspect;

import dev.golddiggerapi.exception.CustomErrorCode;
import dev.golddiggerapi.exception.detail.ApiException;
import dev.golddiggerapi.global.annotation.RateLimit;
import dev.golddiggerapi.global.service.ApiRateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class RateLimiterAspect {

    private final ApiRateLimiter apiRateLimiter;
    private static final String BUCKET_API_KEY_PREFIX = "user:bucket:key:";

    @Around(value = "@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String userId = joinPoint.getArgs()[0].toString();
        if (apiRateLimiter.tryConsume(BUCKET_API_KEY_PREFIX + userId)) {
            return joinPoint.proceed();
        }
        throw new ApiException(CustomErrorCode.API_RATE_LIMIT);
    }
}
