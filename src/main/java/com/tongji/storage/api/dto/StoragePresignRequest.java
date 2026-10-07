package com.tongji.storage.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * OSS 预签名直传请求，由前端在上传知文正文或图片前提交。
 *
 * @param scene 上传场景：{@code knowpost_content} 表示正文，{@code knowpost_image} 表示图片
 * @param postId 目标知文草稿 ID；使用字符串传输以避免 JavaScript 对 64 位雪花 ID 丢失精度
 * @param contentType 文件 MIME 类型，例如 {@code text/markdown} 或 {@code image/png}，必须与实际 PUT 请求一致
 * @param ext 可选文件扩展名；为空时后端根据 contentType 和 scene 推断
 */
public record StoragePresignRequest(
        @NotBlank String scene,
        @NotBlank String postId,
        @NotBlank String contentType,
        String ext
) {}
