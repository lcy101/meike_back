package com.tongji.common.exception;

import lombok.Getter;

/**
 * 对外稳定的业务错误码集合。
 *
 * <p>业务服务抛出 {@link BusinessException} 时携带其中一项，
 * {@code GlobalExceptionHandler} 再将 {@code code} 和可覆盖的提示文本转换为 HTTP 响应。</p>
 */
@Getter
public enum ErrorCode {
    IDENTIFIER_EXISTS("IDENTIFIER_EXISTS", "账号已存在"),
    IDENTIFIER_NOT_FOUND("IDENTIFIER_NOT_FOUND", "账号不存在"),
    ZGID_EXISTS("ZGID_EXISTS", "知光号已存在"),
    VERIFICATION_RATE_LIMIT("VERIFICATION_RATE_LIMIT", "验证码发送过于频繁"),
    VERIFICATION_DAILY_LIMIT("VERIFICATION_DAILY_LIMIT", "验证码发送次数超限"),
    VERIFICATION_NOT_FOUND("VERIFICATION_NOT_FOUND", "验证码不存在或已过期"),
    VERIFICATION_MISMATCH("VERIFICATION_MISMATCH", "验证码错误"),
    VERIFICATION_TOO_MANY_ATTEMPTS("VERIFICATION_TOO_MANY_ATTEMPTS", "验证码尝试次数过多"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "登录凭证错误"),
    PASSWORD_POLICY_VIOLATION("PASSWORD_POLICY_VIOLATION", "密码强度不足"),
    TERMS_NOT_ACCEPTED("TERMS_NOT_ACCEPTED", "请先同意服务条款"),
    REFRESH_TOKEN_INVALID("REFRESH_TOKEN_INVALID", "刷新令牌无效"),
    BAD_REQUEST("BAD_REQUEST", "请求参数错误"),
    INTERNAL_ERROR("INTERNAL_ERROR", "服务器内部错误");

    private final String code;
    // 调用方没有提供更具体提示时使用的默认中文消息
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
