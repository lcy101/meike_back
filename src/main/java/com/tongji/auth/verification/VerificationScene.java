package com.tongji.auth.verification;

/**
 * 验证码使用场景。
 *
 * <p>场景参与 Redis Key 的组成，使同一手机号/邮箱在注册、登录和重置密码流程中的验证码互不干扰。</p>
 */
public enum VerificationScene {
    REGISTER,
    LOGIN,
    RESET_PASSWORD
}
