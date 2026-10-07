package com.tongji.counter.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 计数模块基础配置。
 *
 * <p>启用 Kafka 生产/消费和定时调度，并提供统一使用 String Key/Value 序列化的生产者工厂与模板。
 * CounterEventProducer 使用该模板发送 {@code counter-events}，聚合消费者的定时任务将增量刷入 Redis SDS。</p>
 */
@Configuration
@EnableScheduling // 启用 @Scheduled 定时任务（计数聚合刷写）
@EnableKafka // 启用 Kafka（计数事件生产与消费）
public class CounterConfig {

    /**
     * @param properties application 配置中的 Kafka 生产者参数
     * @return Key/Value 均采用字符串序列化的生产者工厂
     */
    @Bean
    public ProducerFactory<String, String> stringProducerFactory(KafkaProperties properties) {
        var props = properties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new StringSerializer()); // 统一字符串序列化
    }

    /**
     * @param pf 字符串生产者工厂
     * @return 计数事件和 Outbox 桥接共用的 Kafka 操作模板
     */
    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }
}
