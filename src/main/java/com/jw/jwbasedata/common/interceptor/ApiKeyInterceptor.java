package com.jw.jwbasedata.common.interceptor;

import com.jw.jwbasedata.config.ApiKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * API密钥验证拦截器
 *
 * @author jw
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final ApiKeyConfig apiKeyConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 如果不启用API密钥验证，直接通过
        if (!apiKeyConfig.isEnabled()) {
            return true;
        }

        // 获取请求头中的API密钥
        String apiKey = request.getHeader(apiKeyConfig.getHeaderName());

        // 验证API密钥
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.warn("API密钥验证失败：请求头 {} 为空，请求路径：{}", apiKeyConfig.getHeaderName(), request.getRequestURI());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing API key");
            return false;
        }

        if (!apiKey.equals(apiKeyConfig.getKey())) {
            log.warn("API密钥验证失败：密钥不匹配，请求路径：{}", request.getRequestURI());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
            return false;
        }

        log.debug("API密钥验证通过，请求路径：{}", request.getRequestURI());
        return true;
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\": %d, \"message\": \"%s\"}", status, message));
        response.getWriter().flush();
    }
}