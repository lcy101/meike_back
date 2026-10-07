package com.tongji.search.api.dto;

import java.util.List;

/**
 * 搜索联想响应。
 *
 * @param items Elasticsearch Completion Suggester 根据输入前缀返回的候选知文标题列表
 */
public record SuggestResponse(
        List<String> items
) {}
