package com.tongji.profile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 个人资料接口的跨域过滤器配置。
 *
 * <p>仅匹配 {@code /api/v1/profile/**}，允许前端使用 GET/POST/PATCH 访问资料与头像接口。
 * 本类与全局 Security CORS 配置同时存在，实际部署时需确保两处策略保持一致。</p>
 */
@Configuration
public class CorsConfig {

    /** @return 只作用于个人资料路径的 CORS 过滤器 */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许任意来源；如需限制可改为具体域名
        config.setAllowedOriginPatterns(List.of("*"));
        // 允许常见跨域方法，包括预检
        config.setAllowedMethods(List.of("PATCH", "POST", "GET", "OPTIONS"));
        // 允许所有请求头，包含 Authorization、Content-Type 等
        config.setAllowedHeaders(List.of("*"));
        // 不使用跨域凭证（若前端需要携带 Cookie，请改为 true 并限定具体来源）
        config.setAllowCredentials(false);
        // 预检缓存时间
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 仅对 Profile 相关接口开启跨域
        source.registerCorsConfiguration("/api/v1/profile/**", config);
        return new CorsFilter(source);
    }
}
