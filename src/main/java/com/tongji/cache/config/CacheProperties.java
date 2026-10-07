package com.tongji.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存相关配置项。
 *
 * <p>配置前缀：{@code cache}，用于绑定 {@code application.yml} 中的缓存参数。</p>
 */
@Component
@ConfigurationProperties(prefix = "cache")
@Data
public class CacheProperties {
    /** 进程内 Caffeine 缓存配置，分别控制公共 Feed、我的发布和知文详情。 */
    private L2 l2 = new L2();

    /** 热点 Key 统计窗口、分级阈值和 Redis TTL 延长策略。 */
    private Hotkey hotkey = new Hotkey();

    /**
     * 本地 Caffeine 缓存分组。
     *
     * <p>包含 {@code publicCfg}（公共 Feed）、{@code mineCfg}（我的发布）和
     * {@code detailCfg}（知文详情）三组容量与 TTL 配置。</p>
     */
    @Data
    public static class L2 {
        /** 公共首页 Feed 的本地页缓存配置。 */
        private PublicCfg publicCfg = new PublicCfg();

        /** 当前用户“我的发布”的本地页缓存配置。 */
        private MineCfg mineCfg = new MineCfg();

        /** 单篇知文详情的本地缓存配置。 */
        private DetailCfg detailCfg = new DetailCfg();
    }

    /** 公共 Feed 本地缓存参数。 */
    @Data
    public static class PublicCfg {
        /** 写入公共 Feed 本地缓存后的保留秒数。 */
        private int ttlSeconds = 15;

        /** 公共 Feed 本地缓存最大条目数，超过后按 Caffeine 策略逐出。 */
        private long maxSize = 1000;
    }

    /** “我的发布”本地缓存参数。 */
    @Data
    public static class MineCfg {
        /** 写入个人 Feed 本地缓存后的保留秒数。 */
        private int ttlSeconds = 10;

        /** 个人 Feed 本地缓存最大条目数。 */
        private long maxSize = 1000;
    }

    /** 知文详情本地缓存参数。 */
    @Data
    public static class DetailCfg {
        /** 写入详情本地缓存后的保留秒数。 */
        private int ttlSeconds = 30;

        /** 详情本地缓存最大条目数。 */
        private long maxSize = 5000;
    }

    /**
     * 热点 Key 识别参数。
     *
     * <p>包含统计窗口与切片长度、低/中/高三级访问阈值，以及每个级别对应的 TTL 延长秒数。</p>
     */
    @Data
    public static class Hotkey {
        /** 计算访问热度的滑动窗口长度，单位秒。 */
        private int windowSeconds = 60;

        /** 窗口切片长度，单位秒；访问次数按切片累计。 */
        private int segmentSeconds = 10;

        /** 窗口内达到该访问次数后进入低热级别。 */
        private int levelLow = 50;

        /** 窗口内达到该访问次数后进入中热级别。 */
        private int levelMedium = 200;

        /** 窗口内达到该访问次数后进入高热级别。 */
        private int levelHigh = 500;

        /** 低热 Key 在基础 TTL 上额外延长的秒数。 */
        private int extendLowSeconds = 20;

        /** 中热 Key 在基础 TTL 上额外延长的秒数。 */
        private int extendMediumSeconds = 60;

        /** 高热 Key 在基础 TTL 上额外延长的秒数。 */
        private int extendHighSeconds = 120;
    }
}
