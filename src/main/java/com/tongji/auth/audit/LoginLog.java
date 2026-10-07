package com.tongji.auth.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 登录审计实体，对应 MySQL {@code login_logs} 表。
 *
 * <p>记录用户通过注册、密码或验证码渠道访问系统的结果，以及当时的 IP 和 User-Agent；
 * 失败日志中的 {@code userId} 允许为空。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog {

    /** MySQL {@code login_logs} 自增主键。 */
    private Long id;
    /** 已识别的用户 ID；账号不存在等失败场景允许为空。 */
    private Long userId;
    /** 本次认证使用的手机号或邮箱。 */
    private String identifier;
    /** 认证渠道：REGISTER、PASSWORD 或 CODE。 */
    private String channel;
    /** 客户端 IP，优先取代理转发头。 */
    private String ip;
    /** 客户端 User-Agent，用于审计与问题排查。 */
    private String userAgent;
    /** 认证结果：SUCCESS 或 FAILED。 */
    private String status;
    /** 事件发生时间，写入 {@code login_logs.created_at}。 */
    private Instant createdAt;
}
