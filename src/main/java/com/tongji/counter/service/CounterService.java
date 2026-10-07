package com.tongji.counter.service;

import java.util.List;
import java.util.Map;

/**
 * 内容实体互动计数接口。
 *
 * <p>为知文等实体提供点赞/收藏的幂等切换、总数读取与用户状态判断。
 * 当前实现以 Redis Bitmap 保存用户事实，以固定长度 String（SDS）保存汇总计数，
 * 并通过 Kafka 异步聚合增量。</p>
 */
public interface CounterService {
    /**
     * 点赞：仅在之前未点赞时置位并 +1。
     * @param entityType 实体类型，例如 knowpost
     * @param entityId 实体 ID
     * @param userId 操作用户 ID
     * @return 是否发生状态变化（true 表示这次操作生效）
     */
    boolean like(String entityType, String entityId, long userId);

    /**
     * 取消点赞：仅在之前已点赞时清位并 -1。
     * @param entityType 实体类型
     * @param entityId 实体 ID
     * @param userId 操作用户 ID
     * @return 是否发生状态变化（true 表示这次操作生效）
     */
    boolean unlike(String entityType, String entityId, long userId);

    /**
     * 收藏：仅在之前未收藏时置位并 +1。
     * @param entityType 实体类型
     * @param entityId 实体 ID
     * @param userId 操作用户 ID
     * @return 是否发生状态变化
     */
    boolean fav(String entityType, String entityId, long userId);

    /**
     * 取消收藏：仅在之前已收藏时清位并 -1。
     * @param entityType 实体类型
     * @param entityId 实体 ID
     * @param userId 操作用户 ID
     * @return 是否发生状态变化
     */
    boolean unfav(String entityType, String entityId, long userId);

    /**
     * 获取指定指标的计数。
     * @param entityType 实体类型
     * @param entityId 实体 ID
     * @param metrics 指标名列表，例如 like、fav
     * @return 指标名到当前汇总值的映射
     */
    Map<String, Long> getCounts(String entityType, String entityId, List<String> metrics);

    /**
     * 批量读取多个实体的指标，减少 Feed 场景中的 Redis 往返。
     *
     * @param entityType 实体类型
     * @param entityIds 实体 ID 列表
     * @param metrics 指标名列表
     * @return entityId -> (metric -> count) 的两层映射
     */
    Map<String, Map<String, Long>> getCountsBatch(String entityType, List<String> entityIds, List<String> metrics);

    /**
     * 判断指定用户是否点赞（读取分片位图）。
     * @param entityType 实体类型
     * @param entityId 实体 ID
     * @param userId 用户 ID
     * @return 位图中的点赞状态
     */
    boolean isLiked(String entityType, String entityId, long userId);

    /**
     * 判断指定用户是否收藏（读取分片位图）。
     * @param entityType 实体类型
     * @param entityId 实体 ID
     * @param userId 用户 ID
     * @return 位图中的收藏状态
     */
    boolean isFaved(String entityType, String entityId, long userId);
}
