package com.tongji.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 刷新令牌请求。
 * <p>
 * 传入旧的刷新令牌，服务器验证后返回新的访问/刷新令牌对。
 *
 * @param refreshToken 待校验和轮换的旧 Refresh JWT；其 jti 还必须存在于 Redis 白名单
 */
public record TokenRefreshRequest(@NotBlank(message = "刷新令牌不能为空") String refreshToken) {
}
