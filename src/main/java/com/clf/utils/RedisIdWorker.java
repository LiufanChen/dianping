package com.clf.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.clf.utils.RedisConstants.LOGIN_USER_TTL;

/**
 * ClassName: RedisIdWork
 * Package: com.clf.utils
 * Description:
 *
 * @Author clf
 * @Create 2025/6/18 14:06
 * @Version 1.0
 */
@Component
public class RedisIdWorker {
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    private static final int COUNT_BITS = 32;

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextId(String keyPrefix) {
        LocalDateTime now = LocalDateTime.now();
        long nowSeconds = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSeconds - BEGIN_TIMESTAMP;

        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //2025-06-18
        long count = stringRedisTemplate.opsForValue().increment("icr:"+keyPrefix+":"+ date);

        return timeStamp << COUNT_BITS | count ;
    }

}
