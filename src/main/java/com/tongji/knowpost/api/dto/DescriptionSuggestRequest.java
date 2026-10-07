package com.tongji.knowpost.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * AI 摘要建议请求，由 Controller 接收前端提供的知文正文。
 *
 * @param content 用于构造 Prompt 的非空正文；该 DTO 本身不会持久化内容
 */
public record DescriptionSuggestRequest(
        @NotBlank(message = "content 不能为空") String content
) {}
