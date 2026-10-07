package com.tongji.storage.api;

import com.tongji.common.exception.BusinessException;
import com.tongji.common.exception.ErrorCode;
import com.tongji.auth.token.JwtService;
import com.tongji.knowpost.mapper.KnowPostMapper;
import com.tongji.knowpost.model.KnowPost;
import com.tongji.storage.OssStorageService;
import com.tongji.storage.api.dto.StoragePresignRequest;
import com.tongji.storage.api.dto.StoragePresignResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

/**
 * 对象存储直传接口。
 *
 * <p>当前主要函数 {@link #presign(StoragePresignRequest, Jwt)} 为知文正文或图片生成
 * 10 分钟有效的 OSS PUT 预签名地址。调用链为
 * {@code StorageController -> OssStorageService -> 阿里云 OSS}。</p>
 *
 * <p>该接口不接收文件内容，只返回 objectKey、签名 URL 和必须携带的请求头；
 * 前端上传完成后再调用知文确认接口，将 objectKey 等元数据写入 MySQL {@code know_posts}。</p>
 */
@RestController
@RequestMapping("/api/v1/storage")
@Validated
@RequiredArgsConstructor
public class StorageController {

    // 负责生成 OSS PUT 预签名 URL，不经后端转发知文文件
    private final OssStorageService ossStorageService;
    // 从认证 JWT 中提取当前用户 ID，防止客户端伪造上传归属
    private final JwtService jwtService;
    // 查询 know_posts，确认目标草稿确实属于当前用户
    private final KnowPostMapper knowPostMapper;

    /**
     * 获取用于直传的 PUT 预签名 URL。
     *
     * <p>执行流程：</p>
     * <ol>
     *   <li>从 JWT 获取当前用户 ID并解析草稿 ID；</li>
     *   <li>查询 {@code know_posts} 校验草稿归属；</li>
     *   <li>按正文或图片场景生成不会相互覆盖的 objectKey；</li>
     *   <li>请求 OSS 生成签名地址并连同 Content-Type 请求头返回。</li>
     * </ol>
     *
     * @param request 上传场景、草稿 ID、Content-Type 和可选扩展名
     * @param jwt 当前登录用户令牌
     * @return 前端直传所需的 objectKey、PUT URL、请求头和有效秒数
     */
    @PostMapping("/presign")
    public StoragePresignResponse presign(@Valid @RequestBody StoragePresignRequest request,
                                          @AuthenticationPrincipal Jwt jwt) {
        long userId = jwtService.extractUserId(jwt);

        long postId;
        try {
            postId = Long.parseLong(request.postId());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "postId 非法");
        }

        // 权限校验：postId 必须属于当前用户
        KnowPost post = knowPostMapper.findById(postId);
        if (post == null || post.getCreatorId() == null || post.getCreatorId() != userId) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "草稿不存在或无权限");
        }

        String scene = request.scene();
        String objectKey;
        String ext = normalizeExt(request.ext(), request.contentType(), scene);

        if ("knowpost_content".equals(scene)) {
            objectKey = "posts/" + postId + "/content" + ext;
        } else if ("knowpost_image".equals(scene)) {
            String date = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of("UTC")).format(Instant.now());
            String rand = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
            objectKey = "posts/" + postId + "/images/" + date + "/" + rand + ext;
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "不支持的上传场景");
        }

        // 签名仅短期有效；文件仍由前端直接 PUT 到 OSS
        int expiresIn = 600;
        String putUrl = ossStorageService.generatePresignedPutUrl(objectKey, request.contentType(), expiresIn);
        Map<String, String> headers = Map.of("Content-Type", request.contentType());
        return new StoragePresignResponse(objectKey, putUrl, headers, expiresIn);
    }

    /** 根据显式扩展名或 MIME 类型确定对象后缀，未知类型使用安全的兜底后缀。 */
    private String normalizeExt(String ext, String contentType, String scene) {
        if (ext != null && !ext.isBlank()) {
            return ext.startsWith(".") ? ext : "." + ext;
        }
        if ("knowpost_content".equals(scene)) {
            return switch (contentType) {
                case "text/markdown" -> ".md";
                case "text/html" -> ".html";
                case "text/plain" -> ".txt";
                case "application/json" -> ".json";
                default -> ".bin";
            };
        } else {
            return switch (contentType) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> ".img";
            };
        }
    }
}
