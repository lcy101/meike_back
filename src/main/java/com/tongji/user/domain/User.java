package com.tongji.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 用户数据库实体，对应 MySQL {@code users} 表。
 *
 * <p>保存登录标识（手机号/邮箱和密码摘要）以及昵称、头像、知光号等个人资料。
 * {@code tagsJson} 对应表中的 JSON 字段 {@code tags_json}；认证与资料模块共用该实体。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /** users 主键；注册插入后由 MySQL 自增生成。 */
    private Long id;
    /** 手机号登录标识；邮箱注册或未绑定手机时可为空，数据库有唯一索引。 */
    private String phone;
    /** 邮箱登录标识；手机注册或未绑定邮箱时可为空，数据库有唯一索引。 */
    private String email;
    /** PasswordEncoder 生成的 BCrypt 摘要；验证码注册且未设置密码时可为空。 */
    private String passwordHash;
    /** 用户昵称；注册时自动生成，之后可由资料接口修改。 */
    private String nickname;
    /** 头像公开 URL，通常指向 OSS/CDN。 */
    private String avatar;
    /** 个人简介。 */
    private String bio;
    /** 全局唯一的知光号，对应 {@code users.zg_id}。 */
    private String zgId;
    /** 性别文本：MALE、FEMALE、OTHER 或 UNKNOWN。 */
    private String gender;
    /** 用户生日。 */
    private LocalDate birthday;
    /** 用户学校名称。 */
    private String school;
    /** 用户领域标签的 JSON 文本，对应 {@code users.tags_json}。 */
    private String tagsJson;
    /** 账号创建时间。 */
    private Instant createdAt;
    /** 账号或资料最后更新时间。 */
    private Instant updatedAt;
}
