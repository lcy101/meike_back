package com.tongji.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.tongji.user.domain.User;
import java.util.List;

/**
 * 用户数据访问接口，对应 MySQL {@code users} 表。
 *
 * <p>供认证模块按手机号/邮箱查找账号，供个人资料模块查询和局部更新资料；
 * SQL 位于 {@code resources/mapper/UserMapper.xml}。</p>
 */
@Mapper
public interface UserMapper {

    /** 按手机号查询完整用户实体，用于手机号登录与注册查重。 */
    User findByPhone(@Param("phone") String phone);

    /** 按邮箱查询完整用户实体，用于邮箱登录与注册查重。 */
    User findByEmail(@Param("email") String email);

    boolean existsByPhone(@Param("phone") String phone);

    boolean existsByEmail(@Param("email") String email);

    /** 新建用户；插入后 MySQL 自增主键会回填到 {@link User#setId(Long)}。 */
    void insert(User user);

    /** 按主键读取用户，供令牌刷新、当前用户和资料接口使用。 */
    User findById(@Param("id") Long id);

    /** 只更新密码摘要，重置密码业务随后会撤销该用户的全部刷新令牌。 */
    void updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    /** 根据实体中的非空属性局部更新用户资料。 */
    void updateProfile(User user);

    boolean existsByZgIdExceptId(@Param("zgId") String zgId, @Param("excludeId") Long excludeId);

    /** 批量读取用户，用于关注/粉丝列表补充昵称、头像等展示信息。 */
    List<User> listByIds(@Param("ids") List<Long> ids);
}
