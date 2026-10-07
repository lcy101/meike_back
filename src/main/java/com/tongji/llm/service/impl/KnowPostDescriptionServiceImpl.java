package com.tongji.llm.service.impl;

import com.tongji.llm.service.KnowPostDescriptionService;
import com.tongji.common.exception.BusinessException;
import com.tongji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

/**
 * 知文摘要生成服务实现。
 *
 * <p>执行流程：校验正文 → 构造中文编辑 Prompt → 通过 {@link ChatClient} 调用 DeepSeek →
 * 规范化模型文本 → 去除多余引号/尾部标点 → 按 Unicode code point 截断至 50 字。</p>
 *
 * <p>本服务只返回建议文本，不写数据库；摘要由后续知文元数据接口保存到
 * MySQL {@code know_posts.description}。</p>
 */
@Service
@RequiredArgsConstructor
public class KnowPostDescriptionServiceImpl implements KnowPostDescriptionService {

    // LlmConfig 创建的 DeepSeek 对话客户端，负责真正发起模型请求
    private final ChatClient chatClient;

    /**
     * 基于正文生成不超过 50 字的中文描述。
     *
     * @param content 前端提交的知文正文
     * @return 清洗并限制长度后的中文摘要建议
     * @throws BusinessException 正文为空或模型调用失败时抛出
     */
    public String generateDescription(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "正文内容不能为空");
        }
        String system = "你是中文文案编辑。请基于用户提供的知文正文，生成一个中文描述，简洁有吸引力，且不超过50个汉字。不输出解释或多段，只输出结果。";
        String user = "正文如下：\n\n" + content + "\n\n请直接给出不超过50字的中文描述。";

        try {
            String result = chatClient
                    .prompt()
                    .system(system)
                    .user(user)
                    .options(DeepSeekChatOptions.builder()
                            .model("deepseek-chat")
                            .temperature(0.8)
                            .maxTokens(120)
                            .build())
                    .call()
                    .content();
            return postProcess(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "大模型调用失败: " + e.getMessage());
        }
    }

    /** 将不可控的模型输出整理为可直接展示的单行、最多 50 字文本。 */
    private String postProcess(String text) {
        if (text == null) {
            return "";
        }
        String t = Normalizer.normalize(text, Normalizer.Form.NFKC)
                .replaceAll("\r\n|\r|\n", " ")
                .replaceAll("\\s+", " ")
                .trim();

        // 去掉可能的前后引号或多余标点
        t = t.replaceAll("^[\"'“”‘’]+|[\"'“”‘’]+$", "")
             .replaceAll("[。!！?？；;、]+$", "");

        // 截断至 50 字（按 code point 计数）
        int limit = 50;
        int count = t.codePointCount(0, t.length());
        if (count <= limit) {
            return t;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0, added = 0;
        while (i < t.length() && added < limit) {
            int cp = t.codePointAt(i);
            sb.appendCodePoint(cp);
            i += Character.charCount(cp);
            added++;
        }
        return sb.toString();
    }
}
