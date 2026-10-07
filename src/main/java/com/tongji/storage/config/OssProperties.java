package com.tongji.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置对象，绑定 {@code oss.*} 配置项。
 *
 * <p>{@link com.tongji.storage.OssStorageService} 使用这些配置创建 OSS 客户端、
 * 生成前端直传 URL，并拼接对象的公开访问地址。</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OssProperties {
    /** OSS 服务端点，例如 {@code oss-cn-shanghai.aliyuncs.com}。 */
    private String endpoint;
    /** 服务端访问密钥 ID，只用于创建 OSS 客户端和生成签名，不返回给前端。 */
    private String accessKeyId;
    /** 服务端访问密钥 Secret，属于敏感配置，不应写入日志或接口响应。 */
    private String accessKeySecret;
    /** 保存头像、知文正文与图片对象的 OSS Bucket 名称。 */
    private String bucket;
    /** 可选的 CDN/自定义域名；未配置时按 Bucket 与 endpoint 拼接公开 URL。 */
    private String publicDomain;
    /** 服务端上传头像时使用的默认目录；知文对象键由 StorageController 单独生成。 */
    private String folder = "avatars";
}
