package com.tongji.knowpost.mapper;

import com.tongji.knowpost.model.KnowPost;
import com.tongji.knowpost.model.KnowPostDetailRow;

import com.tongji.knowpost.model.KnowPostFeedRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知文数据访问接口，对应 MySQL {@code know_posts} 表。
 *
 * <p>负责草稿创建、内容确认、元数据修改、发布/软删除，以及首页和“我的知文”查询。
 * 正文文件不保存在该表中：表内只记录 OSS 的 {@code content_url}、
 * {@code content_object_key}、ETag、大小与 SHA-256；SQL 位于
 * {@code resources/mapper/KnowPostMapper.xml}。</p>
 */
@Mapper
public interface KnowPostMapper {
    /** 插入仅含 ID、作者和初始状态的草稿记录。 */
    void insertDraft(KnowPost post);

    /** 按知文 ID 查询元数据，用于归属校验和编辑流程。 */
    KnowPost findById(@Param("id") Long id);

    /** 确认 OSS 上传完成后，写入正文对象地址及完整性信息。 */
    int updateContent(KnowPost post);

    /** 更新标题、标签、摘要、图片地址、可见性等非空元数据。 */
    int updateMetadata(KnowPost post);

    /** 将当前作者的草稿置为已发布，并记录发布时间。 */
    int publish(@Param("id") Long id, @Param("creatorId") Long creatorId);

    // 首页 Feed 列表（已发布、公开可见），置顶优先，其次按发布时间倒序。
    List<KnowPostFeedRow> listFeedPublic(@Param("limit") int limit,
                                         @Param("offset") int offset);

    // 我的知文列表（当前用户已发布内容），置顶优先，其次按发布时间倒序。
    List<KnowPostFeedRow> listMyPublished(@Param("creatorId") long creatorId,
                                                                              @Param("limit") int limit,
                                                                              @Param("offset") int offset);

    // 设置置顶
    int updateTop(@Param("id") Long id, @Param("creatorId") Long creatorId, @Param("isTop") Boolean isTop);

    // 设置可见性
    int updateVisibility(@Param("id") Long id, @Param("creatorId") Long creatorId, @Param("visible") String visible);

    // 软删除：仅将 know_posts.status 改为 deleted，保留数据库记录与 OSS 对象
    int softDelete(@Param("id") Long id, @Param("creatorId") Long creatorId);

    // 详情查询（含作者信息）
    KnowPostDetailRow findDetailById(@Param("id") Long id);

    // 统计我的已发布知文数量
    long countMyPublished(@Param("creatorId") long creatorId);

    // 列出我的已发布知文ID列表
    List<Long> listMyPublishedIds(@Param("creatorId") long creatorId);
}
