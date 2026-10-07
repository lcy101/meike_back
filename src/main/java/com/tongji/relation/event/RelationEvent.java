package com.tongji.relation.event;

/**
 * 关注关系 Outbox 事件。
 *
 * <p>序列化后写入 MySQL {@code outbox.payload}，再经 Canal/Kafka 交给关系事件处理器。</p>
 *
 * @param type 事件类型：FollowCreated 或 FollowCanceled
 * @param fromUserId 发起关注或取消关注的用户 ID
 * @param toUserId 被关注或被取消关注的目标用户 ID
 * @param id {@code following} 关系记录 ID；创建事件携带该值供异步插入 follower，取消事件可为空
 */
public record RelationEvent(
        String type,
        Long fromUserId,
        Long toUserId,
        Long id) {
}
