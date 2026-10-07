package com.tongji.auth.api.dto;

import com.tongji.auth.verification.VerificationScene;

/**
 * 发送验证码响应。
 * <p>
 * 返回规范化后的账号、场景，以及验证码有效期（秒）。
 *
 * @param identifier 标准化后的手机号或邮箱；邮箱已去空格并转为小写
 * @param scene 本次验证码用途：注册、登录或重置密码
 * @param expireSeconds Redis 中验证码剩余有效期的初始秒数
 */
public record SendCodeResponse(
        String identifier,
        VerificationScene scene,
        int expireSeconds
) {
}
