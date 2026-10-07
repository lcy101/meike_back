package com.tongji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 知光后端应用启动入口。
 *
 * <p>启动后由 Spring Boot 扫描 {@code com.tongji} 下的认证、知文、计数、
 * 关注关系、搜索、AI/RAG 与对象存储组件，并对外提供 REST API。</p>
 */
@SpringBootApplication
public class ZhiGuangApplication {

    /**
     * 启动 Spring 容器和内嵌 Web 服务器。
     *
     * @param args JVM 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(ZhiGuangApplication.class, args);
    }
}
