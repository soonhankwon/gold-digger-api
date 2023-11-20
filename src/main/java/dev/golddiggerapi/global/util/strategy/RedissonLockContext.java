package dev.golddiggerapi.global.util.strategy;

import dev.golddiggerapi.exception.CustomErrorCode;
import dev.golddiggerapi.exception.detail.ApiException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockContext {

    private final RedissonClient redissonClient;

    public void executeLock(String username, RedissonLockStrategy strategy) {
        RLock lock = redissonClient.getLock(username);
        try {
            // waitTime: 락 대기시간, leaseTime: 해당 시간이 지나면 락 해제
            boolean available = lock.tryLock(0, 3, TimeUnit.SECONDS);
            if (!available) {
                throw new ApiException(CustomErrorCode.CANT_GET_LOCK);
            }
            strategy.call();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
