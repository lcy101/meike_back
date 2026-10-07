package com.tongji.auth.verification;

/**
 * 验证码校验结果状态。
 *
 * <p>{@link com.tongji.auth.service.AuthService} 将这些内部状态转换成稳定的业务错误码。</p>
 */
public enum VerificationCodeStatus {
    SUCCESS,
    NOT_FOUND,
    EXPIRED,
    MISMATCH,
    TOO_MANY_ATTEMPTS
}
