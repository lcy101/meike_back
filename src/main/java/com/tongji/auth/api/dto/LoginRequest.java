package com.tongji.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.tongji.auth.model.IdentifierType;

/**
 * 登录请求。
 * <p>
 * 支持两种渠道：
 * - 验证码登录：填写 {@code code}；
 * - 密码登录：填写 {@code password}（用户已设置时）。
 * {@code identifierType} 指定账号类型（手机号/邮箱），{@code identifier} 为账号值。
 *
 * @param identifierType 登录账号类型，PHONE 或 EMAIL
 * @param identifier 手机号或邮箱，用于查询 MySQL {@code users}
 * @param code 可选登录验证码；与 password 二选一，填写时按 LOGIN 场景校验
 * @param password 可选明文密码；与 code 二选一，只用于和数据库中的 BCrypt 摘要比对
 */
public record LoginRequest(
        @NotNull(message = "账号类型不能为空") IdentifierType identifierType,
        @NotBlank(message = "账号不能为空") String identifier,
        String code,
        String password
) {
}
