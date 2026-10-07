package com.tongji.profile.api.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 资料局部更新请求（PATCH）。
 * <p>
 * 客户端仅需提交欲更新的字段；未提交的字段保持不变。发送相同值的重复请求为幂等操作。
 *
 * @param nickname 新昵称，长度 1 至 64；非空时去除首尾空格后写入 {@code users.nickname}
 * @param bio 新个人简介，最长 512 字符
 * @param gender 新性别，只接受 MALE、FEMALE、OTHER、UNKNOWN，不区分大小写
 * @param birthday 新生日，不能晚于当前日期
 * @param zgId 新知光号，只允许 4 至 32 位字母、数字、下划线，并需通过数据库唯一性检查
 * @param school 新学校名称，最长 128 字符
 * @param tagJson 新用户标签 JSON 文本，写入 {@code users.tags_json}
 */
public record ProfilePatchRequest(
        @Size(min = 1, max = 64, message = "昵称长度需在 1-64 之间") String nickname,
        @Size(max = 512, message = "个人描述长度不能超过 512") String bio,
        @Pattern(regexp = "(?i)MALE|FEMALE|OTHER|UNKNOWN", message = "性别取值为 MALE/FEMALE/OTHER/UNKNOWN") String gender,
        @PastOrPresent(message = "生日不能晚于今天") LocalDate birthday,
        @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "知光号仅支持字母、数字、下划线，长度 4-32") String zgId,
        @Size(max = 128, message = "学校名称长度不能超过 128") String school,
        String tagJson
){ }
