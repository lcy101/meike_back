package com.tongji.auth.audit;

import org.apache.ibatis.annotations.Mapper;

/**
 * 登录审计日志 Mapper，对应 MySQL {@code login_logs} 表。
 * SQL 位于 {@code resources/mapper/LoginLogMapper.xml}。
 */
@Mapper
public interface LoginLogMapper {

    /** 写入一次注册或登录的成功/失败审计记录。 */
    void insert(LoginLog log);
}
