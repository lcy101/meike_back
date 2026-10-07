package com.tongji.knowpost.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 知文正文直传 OSS 完成后的确认请求。
 *
 * <p>该对象不包含正文内容，只把 OSS 上传结果和完整性信息写入 MySQL {@code know_posts}。</p>
 *
 * @param objectKey 正文在 OSS Bucket 中的对象键，来自预签名响应
 * @param etag OSS 上传成功后返回的 ETag，用于识别内容版本
 * @param size 正文字节大小，写入 {@code know_posts.content_size}
 * @param sha256 正文内容的 SHA-256 十六进制摘要，用于完整性校验和 RAG 索引版本判断
 */
public record KnowPostContentConfirmRequest(
        @NotBlank String objectKey,
        @NotBlank String etag,
        @NotNull Long size,
        @NotBlank String sha256
) {}
