package com.tongji.auth.api.dto;

import java.time.LocalDate;

/**
 * 认证用户响应。
 * <p>
 * 面向客户端展示的基础用户信息，供“我是谁”与首页显示使用。
 *
 * @param id 用户主键 ID
 * @param nickname 用户昵称
 * @param avatar 头像 URL，通常指向 OSS/CDN
 * @param phone 手机号；邮箱账号或未绑定手机号时可能为空
 * @param zhId 知光号，对应用户实体的 zgId / MySQL {@code users.zg_id}
 * @param birthday 生日
 * @param school 学校名称
 * @param bio 个人简介
 * @param gender 性别
 * @param tagJson 用户领域标签 JSON，来自 {@code users.tags_json}
 */
public record AuthUserResponse(
        Long id,
        String nickname,
        String avatar,
        String phone,
        String zhId,
        LocalDate birthday,
        String school,
        String bio,
        String gender,
        String tagJson
) {
}
