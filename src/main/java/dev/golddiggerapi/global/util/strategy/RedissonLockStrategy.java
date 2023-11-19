package dev.golddiggerapi.global.util.strategy;

@FunctionalInterface
public interface RedissonLockStrategy {
    void call();
}
