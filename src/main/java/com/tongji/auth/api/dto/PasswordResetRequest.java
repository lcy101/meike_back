package com.tongji.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.tongji.auth.model.IdentifierType;

/**
 * 重置密码请求。
 * <p>
 * 通过验证码校验身份后设置新密码。密码需满足复杂度策略。
 *
 * @param identifierType 待重置账号的类型，PHONE 或 EMAIL
 * @param identifier 待重置账号的手机号或邮箱
 * @param code 重置密码场景验证码
 * @param newPassword 新明文密码；服务校验复杂度后只将 BCrypt 摘要写入 {@code users.password_hash}
 */
public record PasswordResetRequest(
        @NotNull(message = "账号类型不能为空") IdentifierType identifierType,
        @NotBlank(message = "账号不能为空") String identifier,
        @NotBlank(message = "验证码不能为空") String code,
        @NotBlank(message = "新密码不能为空") String newPassword
) {
}
