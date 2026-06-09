package com.jw.jwbasedata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * API密钥配置
 *
 * @author jw
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.api-key")
public class ApiKeyConfig {

    /**
     * API密钥（默认值用于开发环境）
     */
    private String key = "dev-api-key-123456";

    /**
     * 是否启用API密钥验证
     */
    private boolean enabled = true;

    /**
     * 需要验证的请求头名称
     */
    private String headerName = "X-API-KEY";
}