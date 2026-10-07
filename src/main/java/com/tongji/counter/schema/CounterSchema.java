package com.tongji.counter.schema;

import java.util.Map;
import java.util.Set;

/**
 * 内容计数 SDS 的版本、字段布局与指标映射。
 *
 * <p>v1 使用五个 4 字节无符号大端字段：read（预留）、like、fav、comment（预留）、
 * repost（预留）。当前 API 只开放 like 和 fav；读写代码通过这里的索引定位字段偏移。</p>
 */
public final class CounterSchema {

    // 使用 v1 Schema：下标约定（可扩展）
    // 0: read（预留）
    // 1: like
    // 2: fav
    // 3: comment（预留）
    // 4: repost（预留）
    public static final String SCHEMA_ID = "v1";
    public static final int FIELD_SIZE = 4;
    public static final int SCHEMA_LEN = 5;

    public static final int IDX_LIKE = 1;
    public static final int IDX_FAV = 2;

    public static final Map<String, Integer> NAME_TO_IDX = Map.of(
            "like", IDX_LIKE,
            "fav", IDX_FAV
    );

    // 对外 API 可请求的指标集合；预留字段不会暴露
    public static final Set<String> SUPPORTED_METRICS = NAME_TO_IDX.keySet();

    private CounterSchema() {}
}
