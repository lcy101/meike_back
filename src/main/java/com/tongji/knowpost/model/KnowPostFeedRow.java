package com.tongji.knowpost.model;

import lombok.Data;

import java.time.Instant;

/**
 * Feed 联表查询的内部行对象。
 *
 * <p>映射 MySQL {@code know_posts} 与 {@code users} 的列表展示字段；服务层随后解析
 * 标签/图片 JSON，并从 Redis 计数系统补充点赞数、收藏数和当前用户状态后生成 {@code FeedItemResponse}。</p>
 */
@Data
public class KnowPostFeedRow {
    /** 知文 ID。 */
    private Long id;
    /** 知文标题。 */
    private String title;
    /** 知文摘要。 */
    private String description;
    /** {@code know_posts.tags} 的标签数组 JSON。 */
    private String tags;
    /** {@code know_posts.img_urls} 的配图 URL 数组 JSON。 */
    private String imgUrls;
    /** 作者头像 URL，来自 {@code users.avatar}。 */
    private String authorAvatar;
    /** 作者昵称，来自 {@code users.nickname}。 */
    private String authorNickname;
    /** 作者领域标签 JSON，来自 {@code users.tags_json}。 */
    private String authorTagJson;
    /** 知文发布时间，Feed 查询据此倒序排列。 */
    private Instant publishTime;
    /** 是否置顶；“我的发布”查询据此优先排序。 */
    private Boolean isTop;
}
