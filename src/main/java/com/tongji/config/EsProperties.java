package com.tongji.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Elasticsearch 连接与 RAG 索引配置。
 *
 * <p>连接参数绑定 {@code spring.elasticsearch.*}；向量索引名来自
 * {@code spring.ai.vectorstore.elasticsearch.index-name}。内容搜索索引名由搜索模块的常量定义。</p>
 */
@Data
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class EsProperties {
    /** ES 节点 URI 列表；当前客户端兼容方法使用其中第一个地址建立连接。 */
    private List<String> uris;

    /** ES Basic Auth 用户名；为空时不附加认证凭证。 */
    private String username;
    /** ES Basic Auth 密码；只有配置用户名后才会使用。 */
    private String password;

    /** RAG 向量索引名，来自 {@code spring.ai.vectorstore.elasticsearch.index-name}。 */
    @Value("${spring.ai.vectorstore.elasticsearch.index-name:}")
    private String index;

    /** @return 当前代码实际连接的第一个 ES 节点；未配置时返回 {@code null} */
    public String getHost() {
        return (uris == null || uris.isEmpty()) ? null : uris.getFirst();
    }
}
