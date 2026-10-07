package com.tongji.counter.event;

import lombok.Data;

/**
 * 计数事件模型。
 *
 * <p>用于描述一次状态变化导致的计数增量（如点赞 +1 / 取消点赞 -1），
 * 由生产者发送到 Kafka，消费者聚合后折叠到汇总计数。</p>
 */
@Data
public class CounterEvent {
    /** 发生互动的实体类型，例如 {@code knowpost}。 */
    private String entityType;
    /** 发生互动的实体 ID；与 entityType 一起确定 Redis 计数对象。 */
    private String entityId;
    /** 指标名称，当前为 like 或 fav。 */
    private String metric;
    /** 指标在固定结构 SDS 中的字段索引，定义于 CounterSchema.NAME_TO_IDX。 */
    private int idx;
    /** 执行点赞/收藏操作的用户 ID，用于追踪事件来源；事实状态另存于 Bitmap。 */
    private long userId;
    /** 汇总计数变化量：新增行为为 +1，取消行为为 -1。 */
    private int delta;

    public CounterEvent(String entityType, String entityId, String metric, int idx, long userId, int delta) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.metric = metric;
        this.idx = idx;
        this.userId = userId;
        this.delta = delta;
    }

    /** 根据一次真实的位图状态变化创建待发送到 Kafka 的计数事件。 */
    public static CounterEvent of(String entityType, String entityId, String metric, int idx, long userId, int delta) {
        return new CounterEvent(entityType, entityId, metric, idx, userId, delta);
    }
}
