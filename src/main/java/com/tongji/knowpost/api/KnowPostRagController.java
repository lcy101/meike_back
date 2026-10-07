package com.tongji.knowpost.api;

import com.tongji.llm.rag.RagIndexService;
import com.tongji.llm.rag.RagQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * 单篇知文 RAG 问答与索引维护接口。
 *
 * <p>问答链路：Controller → 确保正文已切片入向量索引 → 按 postId 过滤相似片段 →
 * 构造上下文 Prompt → 大模型以 SSE 流式返回答案。向量切片保存在 Elasticsearch；
 * 知文正文仍从 {@code know_posts.content_url} 指向的 OSS 地址读取。</p>
 */
@RestController
@RequestMapping("/api/v1/knowposts")
@Validated
@RequiredArgsConstructor
public class KnowPostRagController {

    // 下载 OSS 正文、切片并写入 Elasticsearch 向量索引
    private final RagIndexService indexService;
    // 检索相关切片、构造 Prompt 并调用模型流式回答
    private final RagQueryService ragQueryService;

    /**
     * 单篇知文 RAG 问答（WebFlux + Flux 流式输出）。
     * 示例：GET /api/v1/knowposts/{id}/qa/stream?question=...&topK=5&maxTokens=1024
     *
     * @param id 问答限定的知文 ID
     * @param question 用户问题
     * @param topK 最多召回的相关切片数
     * @param maxTokens 回答长度预算
     * @return 通过 text/event-stream 逐段推送的模型输出
     */
    @GetMapping(value = "/{id}/qa/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> qaStream(@PathVariable("id") long id,
                                 @RequestParam("question") String question,
                                 @RequestParam(value = "topK", defaultValue = "5") int topK,
                                 @RequestParam(value = "maxTokens", defaultValue = "1024") int maxTokens) {
        return ragQueryService.streamAnswerFlux(id, question, topK, maxTokens);
    }

    /**
     * 手动触发单篇索引重建（返回重建的切片数）。
     *
     * @param id 待重建向量索引的知文 ID
     * @return 写入向量库的切片数量
     */
    @PostMapping("/{id}/rag/reindex")
    public int reindex(@PathVariable("id") long id) {
        return indexService.reindexSinglePost(id);
    }
}
