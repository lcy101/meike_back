package com.tongji.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 知文 AI 能力的模型客户端配置。
 *
 * <p>明确选用 Spring AI 注册的 {@code deepSeekChatModel} 构造共享 {@link ChatClient}；
 * 摘要生成和 RAG 问答通过该客户端调用大模型，本类本身不保存业务数据。</p>
 */
@Configuration
public class LlmConfig {

    /**
     * 创建基于 DeepSeek 模型的对话客户端。
     *
     * @param chatModel Spring AI 自动配置的 DeepSeek 模型
     * @return 供 AI 服务构造 Prompt 并调用模型的客户端
     */
    @Bean
    public ChatClient chatClient(@Qualifier("deepSeekChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
