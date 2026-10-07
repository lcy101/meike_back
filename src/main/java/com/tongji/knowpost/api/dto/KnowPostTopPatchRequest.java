package com.tongji.knowpost.api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 修改知文置顶状态的请求对象。
 *
 * @param isTop 是否置顶，最终写入 {@code know_posts.is_top}
 */
public record KnowPostTopPatchRequest(
        @NotNull Boolean isTop
) {}
