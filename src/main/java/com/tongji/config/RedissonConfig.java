package com.tongji.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * Redisson 客户端配置。
 *
 * <p>复用 Spring Boot 的 Redis 地址、密码和库编号，为计数重建等流程提供分布式锁。
 * 看门狗会在持锁任务仍运行时自动续约，降低耗时重建过程中锁提前失效的风险。</p>
 */
@Configuration
public class RedissonConfig {
    @Value("${counter.rebuild.lock.watchdog-ms:30000}")
    private long lockWatchdogMs;

    /**
     * 创建单节点 Redisson 客户端。
     *
     * @param redisProperties Spring Boot Redis 连接配置
     * @return 供业务代码获取分布式锁的客户端
     */
    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        // 配置 Redisson 的锁看门狗超时，用于自动续约锁
        config.setLockWatchdogTimeout(lockWatchdogMs);
        String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        SingleServerConfig single = config.useSingleServer().setAddress(address);

        if (redisProperties.getPassword() != null && !redisProperties.getPassword().isEmpty()) {
            single.setPassword(redisProperties.getPassword());
        }

        // Spring Boot RedisProperties#getDatabase 返回的是原始 int（默认 0），无需判空
        single.setDatabase(redisProperties.getDatabase());
        return Redisson.create(config);
    }
}
