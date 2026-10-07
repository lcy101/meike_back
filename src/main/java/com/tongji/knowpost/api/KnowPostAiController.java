package com.tongji.knowpost.api;

import com.tongji.knowpost.api.dto.DescriptionSuggestRequest;
import com.tongji.knowpost.api.dto.DescriptionSuggestResponse;
import com.tongji.llm.service.KnowPostDescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 知文 AI 摘要接口。
 *
 * <p>调用关系：{@code KnowPostAiController -> KnowPostDescriptionService -> ChatClient -> DeepSeek}。
 * 前端提交正文，服务构造 Prompt 并返回不超过 50 字的摘要；本接口不负责保存摘要，
 * 客户端可再通过知文元数据更新接口写入 MySQL {@code know_posts.description}。</p>
 */
@RestController
@RequestMapping(path = "/api/v1/knowposts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class KnowPostAiController {

    // 构造摘要 Prompt、调用大模型并清洗输出
    private final KnowPostDescriptionService descriptionService;

    /**
     * 生成不超过 50 字的知文描述。
     * 需要鉴权（默认策略），防止匿名滥用。
     *
     * @param req 包含待总结的知文正文
     * @return 经过清洗和长度限制的摘要建议
     */
    @PostMapping(path = "/description/suggest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DescriptionSuggestResponse suggest(@Valid @RequestBody DescriptionSuggestRequest req) {
        String desc = descriptionService.generateDescription(req.content());
        return new DescriptionSuggestResponse(desc);
    }
}
