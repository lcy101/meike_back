package com.tongji.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.tongji.auth.model.IdentifierType;

/**
 * 注册请求。
 * <p>
 * 字段：账号类型与值、验证码、可选密码、是否同意服务条款。
 * 验证：需通过验证码校验；当提供密码时需通过密码策略校验。
 *
 * @param identifierType 注册账号类型，PHONE 或 EMAIL
 * @param identifier 手机号或邮箱；注册成功后分别写入 {@code users.phone} 或 {@code users.email}
 * @param code 注册场景验证码；校验成功后对应 Redis 验证码 Key 会被删除，不能重复使用
 * @param password 可选初始密码；非空时校验复杂度并只保存 BCrypt 摘要
 * @param agreeTerms 是否同意服务条款；必须为 true 才允许注册
 */
public record RegisterRequest(
        @NotNull(message = "账号类型不能为空") IdentifierType identifierType,
        @NotBlank(message = "账号不能为空") String identifier,
        @NotBlank(message = "验证码不能为空") String code,
        String password,
        boolean agreeTerms
) {
}
