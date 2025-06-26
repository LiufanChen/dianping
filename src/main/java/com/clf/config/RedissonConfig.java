package com.clf.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RedissonConfig
 * Package: com.clf.config
 * Description:
 *
 * @Author clf
 * @Create 2025/6/26 11:28
 * @Version 1.0
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.84.128:6379")
                .setPassword("root");
        // 创建RedissonClient对象
        return Redisson.create(config);
    }
}

