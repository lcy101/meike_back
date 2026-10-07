package com.tongji.counter.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 计数事件生产者。
 *
 * <p>将真正发生状态变化的计数增量事件序列化为 JSON，发送到 Kafka
 * {@code counter-events}，供聚合消费者写入 Redis Hash 并定时折叠到 SDS。</p>
 */
@Service
public class CounterEventProducer {
    // Kafka 消息 Key/Value 均为字符串的发送模板
    private final KafkaTemplate<String, String> kafka;
    // CounterEvent 到 JSON payload 的序列化器
    private final ObjectMapper objectMapper;

    public CounterEventProducer(KafkaTemplate<String, String> kafka, ObjectMapper objectMapper) {
        this.kafka = kafka;
        this.objectMapper = objectMapper;
    }

    /**
     * 发布计数事件到 Kafka。
     * @param event 计数事件（实体类型、ID、指标、delta 等）
     */
    public void publish(CounterEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafka.send(CounterTopics.EVENTS, payload); // 异步写入计数事件主题（幂等生产已在配置启用）
        } catch (JsonProcessingException e) {
            // 生产异常不抛出影响主流程；可接入告警
        }
    }
}
