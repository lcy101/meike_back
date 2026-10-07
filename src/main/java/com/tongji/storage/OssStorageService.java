package com.tongji.storage;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.tongji.storage.config.OssProperties;
import com.tongji.common.exception.BusinessException;
import com.tongji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云 OSS 访问服务。
 *
 * <p>主要函数：</p>
 * <ol>
 *   <li>{@link #uploadAvatar(long, MultipartFile)}：服务端上传用户头像；</li>
 *   <li>{@link #generatePresignedPutUrl(String, String, int)}：为知文正文或图片生成前端直传 URL。</li>
 * </ol>
 *
 * <p>文件数据最终保存在配置的 OSS Bucket；MySQL 只保存返回的 URL、objectKey
 * 以及知文正文的 ETag/大小/摘要等元数据。</p>
 */
@Service
@RequiredArgsConstructor
public class OssStorageService {

    // OSS 端点、Bucket、服务端签名凭证以及公开域名配置
    private final OssProperties props;

    /**
     * 将头像文件直接上传到 OSS，并返回公开访问地址。
     *
     * <p>对象键格式为 {@code {folder}/{userId}-{timestamp}.{ext}}；
     * 上传流读取失败会转换为统一业务异常，OSS 客户端始终在 finally 中关闭。</p>
     *
     * @param userId 当前登录用户 ID，用于隔离头像对象名
     * @param file Controller 接收的头像文件
     * @return OSS/CDN 上的公开头像 URL，随后写入 {@code users.avatar}
     */
    public String uploadAvatar(long userId, MultipartFile file) {
        ensureConfigured();

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String objectKey = props.getFolder() + "/" + userId + "-" + Instant.now().toEpochMilli() + ext;

        OSS client = new OSSClientBuilder().build(props.getEndpoint(), props.getAccessKeyId(), props.getAccessKeySecret());

        try {
            PutObjectRequest request = new PutObjectRequest(props.getBucket(), objectKey, file.getInputStream());
            client.putObject(request);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "头像文件读取失败");
        } finally {
            client.shutdown();
        }

        return publicUrl(objectKey);
    }

    /** 根据自定义域名或 Bucket 默认域名拼接对象公开地址。 */
    private String publicUrl(String objectKey) {
        if (props.getPublicDomain() != null && !props.getPublicDomain().isBlank()) {
            return props.getPublicDomain().replaceAll("/$", "") + "/" + objectKey;
        }
        return "https://" + props.getBucket() + "." + props.getEndpoint() + "/" + objectKey;
    }

    /**
     * 生成用于直传的 PUT 预签名 URL。
     * 客户端必须在上传时设置与签名一致的 Content-Type。
     *
     * @param objectKey 目标对象键
     * @param contentType 上传内容类型（如 text/markdown, image/png）
     * @param expiresInSeconds 有效期秒数（建议 300-900）
     * @return 可直接用于 PUT 上传的预签名 URL
     */
    public String generatePresignedPutUrl(String objectKey, String contentType, int expiresInSeconds) {
        ensureConfigured();
        OSS client = new OSSClientBuilder().build(props.getEndpoint(), props.getAccessKeyId(), props.getAccessKeySecret());
        try {
            Date expiration = new Date(System.currentTimeMillis() + expiresInSeconds * 1000L);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(props.getBucket(), objectKey, HttpMethod.PUT);
            request.setExpiration(expiration);
            if (contentType != null && !contentType.isBlank()) {
                request.setContentType(contentType);
            }
            URL url = client.generatePresignedUrl(request);
            return url.toString();
        } finally {
            client.shutdown();
        }
    }

    /** 在创建 OSS 客户端前检查必需配置，避免以不完整凭证发起远程请求。 */
    private void ensureConfigured() {
        if (props.getEndpoint() == null || props.getAccessKeyId() == null || props.getAccessKeySecret() == null || props.getBucket() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "对象存储未配置");
        }
    }
}
