package com.jw.jwbasedata.config;

import com.jw.jwbasedata.common.interceptor.ApiKeyInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 *
 * @author jw
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册API密钥拦截器，保护所有API端点
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**")  // 保护所有API接口
                .excludePathPatterns(
                        "/swagger-ui/**",    // 排除Swagger UI
                        "/v3/api-docs/**",   // 排除API文档
                        "/error",            // 排除错误端点
                        "/favicon.ico"       // 排除favicon
                );
    }
}