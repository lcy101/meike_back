package com.tongji.search.api.dto;

import com.tongji.knowpost.api.dto.FeedItemResponse;
import java.util.List;

/**
 * 知文搜索响应。
 *
 * @param items 当前页搜索结果；每项复用 FeedItemResponse，并可能在 description 中包含高亮摘要
 * @param nextAfter 下一页 {@code search_after} 游标；由本页最后一条 ES 排序值编码，末页时为空
 * @param hasMore 是否还有下一页结果
 */
public record SearchResponse(
        List<FeedItemResponse> items,
        String nextAfter,
        boolean hasMore
) {}
