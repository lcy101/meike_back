package com.tongji.storage.api.dto;

import java.util.Map;

/**
 * OSS 预签名直传响应，告诉前端应把文件上传到哪里以及上传时必须携带什么信息。
 *
 * @param objectKey 文件在 OSS Bucket 中的对象键；上传确认时需原样回传给后端
 * @param putUrl 带临时签名的 HTTP PUT 地址，前端使用它直接上传文件而不经过应用服务器
 * @param headers 上传请求必须携带的请求头；当前主要包含与签名一致的 Content-Type
 * @param expiresIn 预签名地址剩余有效时间，单位为秒；当前接口返回 600 秒
 */
public record StoragePresignResponse(
        String objectKey,
        String putUrl,
        Map<String, String> headers,
        int expiresIn
) {}
