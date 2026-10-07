package com.tongji.knowpost.api.dto;

/**
 * AI 摘要建议响应。
 *
 * @param description 大模型根据知文正文生成并清洗后的摘要；当前服务限制为最多 50 个字符
 */
public record DescriptionSuggestResponse(
        String description
) {}
