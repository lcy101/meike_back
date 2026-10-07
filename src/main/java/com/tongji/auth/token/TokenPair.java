package com.tongji.auth.token;

import java.time.Instant;

/**
 * 访问令牌与刷新令牌的组合。
 *
 * <p>这是认证服务内部结果对象，随后会映射为对外的 {@code TokenResponse}。</p>
 *
 * @param accessToken 访问 JWT，作为 Bearer 凭证调用业务接口
 * @param accessTokenExpiresAt 访问令牌的绝对过期时间
 * @param refreshToken 刷新 JWT，只用于换取新令牌或登出
 * @param refreshTokenExpiresAt 刷新令牌的绝对过期时间
 * @param refreshTokenId 刷新令牌的 jti；作为 Redis 白名单 Key 的一部分用于校验和撤销
 */
public record TokenPair(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt,
        String refreshTokenId
) {
}
