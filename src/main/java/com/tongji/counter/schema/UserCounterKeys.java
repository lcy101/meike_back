package com.tongji.counter.schema;

/**
 * 用户维度计数 Redis Key 生成工具。
 *
 * <p>{@code ucnt:{userId}} 对应 String/SDS，五个连续的 4 字节字段依次保存
 * 关注数、粉丝数、发文数、获赞数和获收藏数。</p>
 */
public final class UserCounterKeys {
    private UserCounterKeys() {}

    /** @return 指定用户的固定结构计数 Key */
    public static String sdsKey(long userId) {
        return "ucnt:" + userId;
    }
}
