package com.tongji.knowpost.api.dto;

/**
 * 创建知文草稿响应。
 *
 * @param id 新建草稿的 64 位雪花 ID；以字符串返回，避免 JavaScript 数值精度丢失
 */
public record KnowPostDraftCreateResponse(String id) {

}
