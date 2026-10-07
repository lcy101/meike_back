package com.tongji.knowpost.model;

import lombok.Data;

import java.time.Instant;

/**
 * 知文详情联表查询的内部行对象。
 *
 * <p>由 {@code KnowPostMapper.findDetailById} 将 MySQL {@code know_posts} 与
 * {@code users} 的作者字段一次映射到该对象，再由服务层解析 JSON、补充 Redis 计数并转换为响应 DTO。
 * 该对象不直接返回给客户端，也不会再次持久化。</p>
 */
@Data
public class KnowPostDetailRow {
    /** 知文 ID，来自 {@code know_posts.id}。 */
    private Long id;
    /** 作者 ID，来自 {@code know_posts.creator_id}。 */
    private Long creatorId;
    /** 知文标题。 */
    private String title;
    /** 知文摘要。 */
    private String description;
    /** 标签数组 JSON，服务层解析为 List。 */
    private String tags;
    /** 配图 URL 数组 JSON，服务层解析为 List。 */
    private String imgUrls;
    /** OSS 正文访问 URL。 */
    private String contentUrl;
    /** OSS 正文 ETag，用于 RAG 索引版本判断。 */
    private String contentEtag;
    /** 正文 SHA-256 摘要，用于 RAG 索引版本判断。 */
    private String contentSha256;
    /** 作者头像 URL，来自 {@code users.avatar}。 */
    private String authorAvatar;
    /** 作者昵称，来自 {@code users.nickname}。 */
    private String authorNickname;
    /** 作者领域标签 JSON，来自 {@code users.tags_json}。 */
    private String authorTagJson;
    /** 知文发布时间。 */
    private Instant publishTime;
    /** 是否置顶。 */
    private Boolean isTop;
    /** 可见性，用于详情访问权限判断。 */
    private String visible;
    /** 内容类型。 */
    private String type;
    /** 生命周期状态，用于排除删除内容并判断是否公开发布。 */
    private String status;
}
