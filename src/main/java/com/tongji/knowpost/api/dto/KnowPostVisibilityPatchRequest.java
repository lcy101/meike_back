package com.tongji.knowpost.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 修改知文可见性的请求对象。
 *
 * @param visible 新可见性；具体允许值由知文服务校验并写入 {@code know_posts.visible}
 */
public record KnowPostVisibilityPatchRequest(
        @NotBlank String visible
) {}
