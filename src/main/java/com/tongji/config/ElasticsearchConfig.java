package com.tongji.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Elasticsearch 官方 Java 客户端配置。
 *
 * <p>读取 {@link EsProperties} 建立底层 REST 连接，并向内容搜索、联想建议、
 * 搜索索引同步等服务提供类型安全的 {@link ElasticsearchClient}。</p>
 */
@Configuration
@EnableConfigurationProperties(EsProperties.class)
@RequiredArgsConstructor
public class ElasticsearchConfig {

    // ES 连接参数；是否携带 Basic Auth 由 username 是否为空决定
    private final EsProperties props;

    /**
     * 创建项目共享的 Elasticsearch 客户端。
     *
     * @return 搜索和索引服务使用的类型安全客户端
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        BasicCredentialsProvider creds = new BasicCredentialsProvider();

        if (StringUtils.hasText(props.getUsername())) {
            creds.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(props.getUsername(), props.getPassword()));
        }

        RestClientBuilder builder = RestClient.builder(org.apache.http.HttpHost.create(props.getHost()))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(creds));

        RestClient restClient = builder.build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}
