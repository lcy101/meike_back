package com.tongji.auth.verification;

/**
 * 发送验证码结果。
 * <p>
 * 返回规范化账号、发送场景与验证码有效期（秒）。
 *
 * @param identifier 已完成标准化的手机号或邮箱
 * @param scene 验证码使用场景
 * @param expireSeconds 验证码 Redis Hash 的初始有效秒数
 */
public record SendCodeResult(String identifier,
                             VerificationScene scene,
                             int expireSeconds
) {
}
