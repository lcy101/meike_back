package com.tongji.search.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongji.relation.outbox.OutboxTopics;
import com.tongji.common.util.OutboxMessageUtil;
import com.tongji.search.index.SearchIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索索引的 Outbox 消费者。
 *
 * <p>监听 Kafka {@code canal-outbox}，从 Canal 行变更中提取 payload，
 * 仅处理 {@code entity=knowpost} 的 upsert 与 delete，并交给 SearchIndexService 幂等覆盖 ES 文档。
 * 整批成功后才手动提交位点。</p>
 */
@Service
@RequiredArgsConstructor
public class CanalOutboxConsumerSearch {
    // 解析 Canal 外层消息和 Outbox payload JSON
    private final ObjectMapper objectMapper;
    // 执行 Elasticsearch 文档 upsert 或软删除
    private final SearchIndexService indexService;

    /**
     * 消费 outbox 消息，解析合法行并按实体类型更新索引。
     */
    @KafkaListener(topics = OutboxTopics.CANAL_OUTBOX, groupId = "search-index-consumer")
    public void onMessage(String message, Acknowledgment ack) {
        try {
            List<JsonNode> rows = OutboxMessageUtil.extractRows(objectMapper, message);

            if (rows.isEmpty()) {
                ack.acknowledge();
                return;
            }

            for (JsonNode row : rows) {
                JsonNode payloadNode = row.get("payload");
                if (payloadNode == null) {
                    continue;
                }

                JsonNode payload = objectMapper.readTree(payloadNode.asText());
                String entity = text(payload.get("entity"));
                String op = text(payload.get("op"));
                Long id = asLong(payload.get("id"));
                if (!"knowpost".equals(entity) || id == null) {
                    continue;
                }

                // 软删与 upsert，均覆盖写入同一文档 ID，保证幂等
                if ("delete".equalsIgnoreCase(op)) {
                    indexService.softDeleteKnowPost(id);
                } else {
                    indexService.upsertKnowPost(id);
                }
            }
            // 提交位点，确保“已处理”的语义
            ack.acknowledge();
        } catch (Exception ignored) {}
    }

    private String text(JsonNode n) {
        return n == null ? null : n.asText();
    }

    private Long asLong(JsonNode n) {
        if (n == null) {
            return null;
        }

        try {
            return Long.parseLong(n.asText());
        } catch (Exception e) {
            return null;
        }
    }
}
