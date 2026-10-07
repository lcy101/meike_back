package com.tongji.auth.api.dto;

/**
 * 认证响应。
 * <p>
 * 登录/注册成功后返回：包含用户信息与令牌信息的组合结果。
 *
 * @param user 当前登录用户可展示的资料
 * @param token 新签发的 Access/Refresh Token 及各自过期时间
 */
public record AuthResponse(
        AuthUserResponse user,
        TokenResponse token
) {
}
