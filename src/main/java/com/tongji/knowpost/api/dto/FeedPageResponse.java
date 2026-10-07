package com.tongji.knowpost.api.dto;

import java.util.List;

/**
 * 知文 Feed 分页响应，公共首页和“我的发布”接口共用。
 *
 * @param items 当前页的知文摘要条目；可能来自 Caffeine、Redis 片段缓存或 MySQL 回源
 * @param page 当前页码，从 1 开始
 * @param size 请求采用的每页条数，服务端限制在 1 至 50
 * @param hasMore 当前页之后是否还有数据，客户端据此决定是否继续翻页
 */
public record FeedPageResponse(
        List<FeedItemResponse> items,
        int page,
        int size,
        boolean hasMore
) {}
