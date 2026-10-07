package com.tongji.knowpost.api.dto;

import java.time.Instant;
import java.util.List;

/**
 * 知文详情响应，由 MySQL 知文/作者数据、OSS 正文地址和 Redis 实时互动状态共同组装。
 *
 * @param id 知文雪花 ID，以字符串返回避免前端精度丢失
 * @param title 知文标题
 * @param description 知文摘要
 * @param contentUrl 正文访问地址，实际内容保存在 OSS
 * @param images 配图 URL 列表，解析自 {@code know_posts.img_urls} JSON
 * @param tags 知文标签列表，解析自 {@code know_posts.tags} JSON
 * @param authorId 作者用户 ID，以字符串返回
 * @param authorAvatar 作者头像 URL
 * @param authorNickname 作者昵称
 * @param authorTagJson 作者领域标签 JSON，来自 {@code users.tags_json}
 * @param likeCount 知文点赞总数，来自 Redis 内容计数 SDS
 * @param favoriteCount 知文收藏总数，来自 Redis 内容计数 SDS
 * @param liked 当前登录用户是否已点赞；匿名访问时为 false
 * @param faved 当前登录用户是否已收藏；匿名访问时为 false
 * @param isTop 作者是否将该知文置顶
 * @param visible 知文可见性
 * @param type 内容类型，当前默认 {@code image_text}
 * @param publishTime 发布时间；未发布内容可能为空
 */
public record KnowPostDetailResponse(
        String id,
        String title,
        String description,
        String contentUrl,
        List<String> images,
        List<String> tags,
        String authorId,
        String authorAvatar,
        String authorNickname,
        String authorTagJson,
        Long likeCount,
        Long favoriteCount,
        Boolean liked,
        Boolean faved,
        Boolean isTop,
        String visible,
        String type,
        Instant publishTime
) {}
