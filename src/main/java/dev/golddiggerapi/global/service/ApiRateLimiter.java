package dev.golddiggerapi.global.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ApiRateLimiter {

    private final LettuceBasedProxyManager<String> proxyManager;
    private final ConcurrentHashMap<String, Bucket> cache = new ConcurrentHashMap<>();

    public ApiRateLimiter(RedisClient redisClient) {
        // Redis 연결을 생성
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        // Redis 연결을 이용해 LettuceBasedProxyManager 객체 생성
        this.proxyManager = LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(100)))
                .build();
    }

    /**
     * API 키에 해당하는 버킷을 가져오거나, 없을 경우 새로 생성
     *
     * @param apiKey API Key
     * @return API Key : Bucket
     */
    private Bucket getOrCreateBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, key -> {
            // 버킷 ID와 버킷 설정을 이용해 버킷을 생성하고, 이를 반환
            return proxyManager.builder().build(key, this::createBucketConfiguration);
        });
    }

    /**
     * 버킷 설정을 생성하는 메서드
     * @return 생성된 버킷 설정
     */
    private BucketConfiguration createBucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder().capacity(1)
                        .refillIntervally(1, Duration.ofSeconds(1)).build())
                .build();
    }

    /**
     * API 키에 해당하는 버킷에서 토큰을 소비하려고 시도하는 메서드
     * @param apiKey API 키
     * @return 토큰 소비 성공 여부
     */
    public boolean tryConsume(String apiKey) {
        // API 키에 해당하는 버킷을 가져옴
        Bucket bucket = getOrCreateBucket(apiKey);
        // 버킷에서 토큰을 소비하려고 시도하고, 그 결과를 반환
        boolean consumed = bucket.tryConsume(1);
        log.info("API Key: {}, Consumed: {}, Time: {}", apiKey, consumed, LocalDateTime.now());
        return consumed;
    }
}
