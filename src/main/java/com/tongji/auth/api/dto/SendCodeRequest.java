package com.tongji.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.tongji.auth.model.IdentifierType;
import com.tongji.auth.verification.VerificationScene;

/**
 * 发送验证码请求。
 * <p>
 * {@code scene} 指定场景（注册/登录/重置密码），配合账号类型与值用于生成并发送验证码。
 *
 * @param scene 验证码使用场景；不同场景使用相互隔离的 Redis Key
 * @param identifierType 账号类型，PHONE 表示手机号、EMAIL 表示邮箱
 * @param identifier 接收验证码的手机号或邮箱，服务会先校验格式并标准化
 */
public record SendCodeRequest(
        @NotNull(message = "场景不能为空") VerificationScene scene,
        @NotNull(message = "账号类型不能为空") IdentifierType identifierType,
        @NotBlank(message = "账号不能为空") String identifier
) {
}
