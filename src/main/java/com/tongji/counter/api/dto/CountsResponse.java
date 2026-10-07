package com.tongji.counter.api.dto;

import lombok.Data;

import java.util.Map;

/**
 * 计数响应体：返回实体类型、ID 及各指标的计数值。
 */
@Data
public class CountsResponse {
    /** 被查询的实体类型，例如 {@code knowpost}。 */
    private String entityType;
    /** 被查询的实体 ID；使用字符串兼容不同实体 ID 格式并避免前端精度问题。 */
    private String entityId;
    /** 指标名到计数值的映射；当前支持 like 和 fav，数值来自 Redis SDS。 */
    private Map<String, Long> counts;

    /**
     * 构造响应。
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param counts 指标到计数值的映射
     */
    public CountsResponse(String entityType, String entityId, Map<String, Long> counts) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.counts = counts;
    }
}
