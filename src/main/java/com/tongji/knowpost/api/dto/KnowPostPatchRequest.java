package com.tongji.knowpost.api.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 知文元数据局部更新请求，所有字段均可选，只更新前端实际提交的内容。
 *
 * @param title 知文标题，写入 {@code know_posts.title}
 * @param tagId 主分类 ID，写入 {@code know_posts.tag_id}
 * @param tags 标签文本列表，最多 20 项；服务层序列化为 JSON 写入 {@code know_posts.tags}
 * @param imgUrls 知文配图 URL 列表，最多 20 项；图片本体在 OSS，URL 列表以 JSON 写入 MySQL
 * @param visible 可见性，可使用 public、followers、school、private 或 unlisted
 * @param isTop 是否在“我的知文”列表置顶，写入 {@code know_posts.is_top}
 * @param description 知文摘要，可由用户填写或采用 AI 摘要建议，写入 {@code know_posts.description}
 */
public record KnowPostPatchRequest(
        String title,
        Long tagId,
        @Size(max = 20) List<String> tags,
        @Size(max = 20) List<String> imgUrls,
        String visible,
        Boolean isTop,
        String description
) {}
