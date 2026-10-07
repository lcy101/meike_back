package com.tongji.auth.model;

/**
 * 用户登录标识类型。
 *
 * <p>认证接口据此选择 {@code users.phone} 或 {@code users.email} 查询账号，
 * 并采用对应的格式校验与标准化规则。</p>
 */
public enum IdentifierType {
    PHONE,
    EMAIL;

    /**
     * 将接口中的文本类型转换为枚举，兼容 {@code mobile} 作为手机号别名。
     *
     * @param value 客户端提交的标识类型
     * @return PHONE 或 EMAIL
     * @throws IllegalArgumentException 类型缺失或不受支持时抛出
     */
    public static IdentifierType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("identifier type required");
        }
        return switch (value.toLowerCase()) {
            case "phone", "mobile" -> PHONE;
            case "email" -> EMAIL;
            default -> throw new IllegalArgumentException("Unsupported identifier type: " + value);
        };
    }
}
