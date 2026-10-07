package com.tongji.knowpost.api.dto;

import java.util.List;

/**
 * Feed 中的一条知文摘要。
 *
 * @param id 知文 ID，以字符串返回避免前端精度丢失
 * @param title 知文标题
 * @param description 知文摘要；搜索结果中可能替换为命中高亮片段
 * @param coverImage 第一张配图的 URL；没有图片时为空
 * @param tags 知文标签列表
 * @param authorAvatar 作者头像 URL
 * @param authorNickname 作者昵称
 * @param tagJson 作者领域标签 JSON，来自 {@code users.tags_json}
 * @param likeCount 点赞总数，来自 Redis 计数或 Elasticsearch 中的同步快照
 * @param favoriteCount 收藏总数，来自 Redis 计数或 Elasticsearch 中的同步快照
 * @param liked 当前登录用户是否已点赞；公共缓存中的基础对象通常为 null，返回前实时补充
 * @param faved 当前登录用户是否已收藏；公共缓存中的基础对象通常为 null，返回前实时补充
 * @param isTop 是否置顶；“我的发布”列表会使用该字段
 */
public record FeedItemResponse(
        String id,
        String title,
        String description,
        String coverImage,
        List<String> tags,
        String authorAvatar,
        String authorNickname,
        String tagJson,
        Long likeCount,
        Long favoriteCount,
        Boolean liked,
        Boolean faved,
        Boolean isTop
) {}
