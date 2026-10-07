package com.tongji.auth.verification;

/**
 * 验证码校验结果。
 * <p>
 * 包含状态（成功/未找到/过期/错误/尝试过多）和次数统计信息，提供便捷成功判断。
 *
 * @param status 本次校验状态，认证服务据此转换为对应业务错误码
 * @param attempts 当前已发生的失败尝试次数；首次校验前为 0
 * @param maxAttempts 该验证码允许的最大失败次数，来自认证配置并随验证码保存在 Redis Hash
 */
public record VerificationCheckResult(
        VerificationCodeStatus status,
        int attempts,
        int maxAttempts
) {
    /** @return status 是否为 SUCCESS */
    public boolean isSuccess() {
        return status == VerificationCodeStatus.SUCCESS;
    }
}
