package com.tongji.knowpost.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 知文数据库实体，对应 MySQL {@code know_posts} 表。
 *
 * <p>保存标题、摘要、标签、发布状态以及 OSS 对象元数据。正文二进制不进入 MySQL，
 * 只记录 {@code contentUrl/contentObjectKey/contentEtag/contentSize/contentSha256}；
 * {@code tags} 和 {@code imgUrls} 是与 JSON 列对应的字符串。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowPost {
    /** 知文主键，由业务层雪花算法生成，不使用数据库自增。 */
    private Long id;
    /** 主分类 ID，对应 {@code know_posts.tag_id}。 */
    private Long tagId;
    /** 标签数组的 JSON 字符串，例如 {@code ["java","编程"]}。 */
    private String tags;
    /** 知文标题。 */
    private String title;
    /** 最多 50 字的摘要，可由 AI 建议或用户编辑。 */
    private String description;
    /** OSS 正文的公开访问 URL；正文内容本身不保存在 MySQL。 */
    private String contentUrl;
    /** 正文在 OSS Bucket 中的 objectKey。 */
    private String contentObjectKey;
    /** OSS 返回的正文 ETag，用于识别对象版本。 */
    private String contentEtag;
    /** 正文字节大小。 */
    private Long contentSize;
    /** 正文 SHA-256 十六进制摘要，用于完整性校验和 RAG 索引版本判断。 */
    private String contentSha256;
    /** 作者用户 ID，关联 MySQL {@code users.id}。 */
    private Long creatorId;
    /** 作者是否将知文置顶，主要影响“我的发布”排序。 */
    private Boolean isTop;
    /** 内容类型，当前默认 {@code image_text}。 */
    private String type;
    /** 可见性：public、followers、school、private 或 unlisted。 */
    private String visible;
    /** OSS 配图 URL 数组的 JSON 字符串，例如 {@code ["https://..."]}。 */
    private String imgUrls;
    /** 视频 URL；数据库已预留，一期功能暂未使用。 */
    private String videoUrl;
    /** 生命周期状态：draft、published、deleted 等。 */
    private String status;
    /** 草稿创建时间。 */
    private Instant createTime;
    /** 最近一次内容、元数据或状态更新时间。 */
    private Instant updateTime;
    /** 正式发布时间；草稿尚未发布时为空。 */
    private Instant publishTime;
}
