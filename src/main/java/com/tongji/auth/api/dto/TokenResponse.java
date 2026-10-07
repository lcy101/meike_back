package com.tongji.auth.api.dto;

import java.time.Instant;

/**
 * 令牌响应。
 * <p>
 * 返回访问令牌与刷新令牌及其过期时间，供客户端持久化与后续调用使用。
 *
 * @param accessToken 短期访问 JWT，客户端通过 Authorization Bearer 请求头访问受保护接口
 * @param accessTokenExpiresAt 访问令牌绝对过期时间，默认签发后 15 分钟
 * @param refreshToken 长期刷新 JWT，仅提交给刷新或登出接口，不作为业务接口凭证
 * @param refreshTokenExpiresAt 刷新令牌绝对过期时间，默认签发后 7 天
 */
public record TokenResponse(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt
) {
}
