package com.tongji.counter.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 行为请求体：用于点赞/收藏等操作的实体标识。
 */
@Data
public class ActionRequest {
    /** 目标实体类型，例如 {@code knowpost}；用于组成 Redis Bitmap/SDS Key。 */
    @NotBlank
    private String entityType;
    /** 目标实体 ID，例如知文雪花 ID；用户 ID 从 JWT 获取，不由该请求提供。 */
    @NotBlank
    private String entityId;
}
