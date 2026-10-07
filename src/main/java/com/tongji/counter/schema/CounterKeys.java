package com.tongji.counter.schema;

/**
 * 内容计数模块 Redis Key 生成工具。
 *
 * <p>集中生成 SDS 汇总、分片 Bitmap 事实和待折叠 Hash 聚合桶的键名，
 * 防止生产者、消费者与重建流程使用不一致的格式。</p>
 */
public final class CounterKeys {
    private CounterKeys() {}

    /**
     * @return {@code cnt:{schema}:{entityType}:{entityId}}，类型为固定长度 String/SDS
     */
    public static String sdsKey(String entityType, String entityId) {
        return String.format("cnt:%s:%s:%s", CounterSchema.SCHEMA_ID, entityType, entityId);
    }

    /**
     * @return {@code bm:{metric}:{entityType}:{entityId}:{chunk}}，类型为 Bitmap
     */
    public static String bitmapKey(String metric, String entityType, String entityId, long chunk) {
        return String.format("bm:%s:%s:%s:%d", metric, entityType, entityId, chunk);
    }

    /**
     * @return {@code agg:{schema}:{entityType}:{entityId}}，类型为待刷写增量 Hash
     */
    public static String aggKey(String entityType, String entityId) {
        return String.format("agg:%s:%s:%s", CounterSchema.SCHEMA_ID, entityType, entityId);
    }
}
