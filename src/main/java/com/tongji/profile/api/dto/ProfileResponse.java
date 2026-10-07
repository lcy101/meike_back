package com.tongji.profile.api.dto;

import java.time.LocalDate;

/**
 * 个人资料响应对象，将 {@code users} 表中允许对客户端展示的字段返回给前端。
 *
 * @param id 用户 ID
 * @param nickname 昵称
 * @param avatar 头像 URL，通常指向 OSS/CDN
 * @param bio 个人简介
 * @param zgId 全局唯一的知光号
 * @param gender 性别
 * @param birthday 生日
 * @param school 学校
 * @param phone 手机号
 * @param email 邮箱
 * @param tagJson 用户标签 JSON 文本
 */
public record ProfileResponse(
        Long id,
        String nickname,
        String avatar,
        String bio,
        String zgId,
        String gender,
        LocalDate birthday,
        String school,
        String phone,
        String email,
        String tagJson
) {}
